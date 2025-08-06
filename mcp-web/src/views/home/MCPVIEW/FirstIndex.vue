<script>
import {mapGetters, mapMutations} from "vuex";

import editMcpDialog from "./dialog/editMcpDialog.vue";

export default {
  components: {
    mcpServer: () => import('./McpServer.vue'),
    myMcp: () => import('./MyMcp.vue'),
    mcpTotal: () => import('./McpTotal.vue'),
  },
  data() {
    return {
      serverClass: [],
      rParams: {
        "index": 1,
        "pageSize": 9999,
        "serverName": "",
        "serverClass": "",
        "serverStatus": "已发布",
        "userId": null
      },
      list: [],
      total: 0,
      listFlag: true,
      endijg: false,
      titleImgs: [],
      weishenheNUM: -1,// -1不是管理员
      createType: "",
      activePage: 'mcpIndex',
      activedClass: '',// 左侧菜单选中的值
      indexKey: 0, // 首页的key
    }
  },
  methods: {
    ...mapMutations(["logout", "setPower"]),

    goDetail(item) {
      this.$router.push({
        name: 'mcpDetail',
        query: {
          mcpId: item.seqId
        }
      });
    },
    // 获取分类字典值接口
    async getServerClass() {
      let resp = await ajax
          .get(`${this.$config.gateUrl}/mcpServer/getServerClass`);
      this.serverClass = resp.data;
      if (this.serverClass != null && this.serverClass.length > 0) {
        this.activedClass = this.serverClass[0].value
      }
    },
    isSelected(label) {
      return this.rParams.serverClass.findIndex(item => item == label) != -1;
    },
    clickLabel(item) {
      // item {label,value}
      let ele = document.getElementsByClassName("boxaaa" + item.value)[0];
      if (ele == null) {
        return;
      }
      ele.scrollIntoView({behavior: 'smooth', block: 'start'});

      this.activedClass = item.value;


    },
    async searchTable() {
      this.listFlag = true;
      let rp = structuredClone(this.rParams);
      try {
        for (let i = 0; i < this.serverClass.length; i++) {
          const element = this.serverClass[i];
          rp.serverClass = element.value;


          let resp = await ajax
              .post(`${this.$config.gateUrl}/mcpServer/queryMcpServerInfoList`, rp);
          this.list.push({
            label: element.label,
            tagValue: element.value,
            list: resp.data.records,
            total: resp.data.total
          });


        }


      } catch (error) {
        this.endijg = true;
        console.log(error);

      } finally {

        this.listFlag = false;
      }


    },
    searchTable2() {
      this.rParams.index = 1;
      this.listFlag = true;
      this.endijg = false;
      this.total = 0;
      this.list = [];
      this.searchTable();
    },
    async randomIcon() {
      // /mcpServer/randomIcon
      let resp = await ajax
          .post(`${this.$config.gateUrl}/mcpServer/randomIcon`);
      this.titleImgs = resp.data;

    },
    addPlus() {
      this.$dialog(editMcpDialog, {
        seqId: "",
        usermsg: this.user
      }).then((r) => {
        this.searchTable2();
      });
    },
    async getNum() {
      let resp = await ajax
          .post(`${this.$config.gateUrl}/mcpServer/queryMcpServerCount`, {
            "serverStatus": "待审核"
          });

      this.weishenheNUM = resp.data;
    },
    goManage() {
      // window.open(`/#/operation/McpServer?serverStatus=111`);
      // location.href = `/#/operation/McpServer?serverStatus=111`;
      window.open(`/#/operation/McpServer?serverStatus=111`);
    },
    geturl() {
      // 获取当前 URL
      let currentUrl = window.location.href;
      let i = currentUrl.indexOf("createType");
      let nstr;
      if (i !== -1) {
        nstr = currentUrl.slice(i + "createType=".length, i + "createType=".length + 3);

        this.createType = nstr;
        this.$dialog(editMcpDialog, {
          seqId: "",
          usermsg: this.user,
          createType: this.createType
        }).then((r) => {
          this.searchTable2();
        });
      }
    },

    async onMenu(path, domId) {
      this.activePage = path;
      if (path == 'mcpIndex') {
        this.list = [];
        await this.getServerClass();
        this.searchTable();
      }


    },
    examine(path) {
      window.sessionStorage.setItem('magne', "待审核");
      this.activePage = path;

    },

    handleCommand(cmd) {
      switch (cmd) {
        case "logout":
          this.$confirm("确定要退出登录吗？", "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          }).then(() => {
            ajax
                .post(`${this.$config.gateUrl}/login/logout`, {
                  redirectToLogin: "0",
                })
                .then((r) => {
                  if (r.code == 1000) {
                    localStorage.removeItem("token");
                    this.logout();
                    this.user = {};
                    location.href = `/#/index`;
                    this.authMenu = [];
                  } else {
                    this.$message.error(r.msg);
                  }
                });
          });
        default:
          break;
      }
    },
  },
  async created() {
    await this.getServerClass();
    this.searchTable();
    this.randomIcon();
    this.getNum();
    this.geturl();
  },
  mounted() {
  }
}
</script>
<template>
  <div>

    <!-- head -->

    <el-header class="flex-between-center header">
      <div class="flex-align-center">
        <div class="logo mgr20"><img alt="" src="/static/homepage/中国电信.png"/></div>
        <el-menu :default-active="activePage" active-text-color="#2DDBF0" background-color="#0000" mode="horizontal">
          <el-menu-item index="mcpIndex" @click="
            onMenu(
              `mcpIndex`,
              '启明网络大模型系统导航'
            )">
            <a href=" javascript:void(0)">MCP广场</a>
            <!-- 意愿自治门户首页 -->
          </el-menu-item>
        </el-menu>
      </div>
    </el-header>


    <transition mode="out-in" name="fade">
      <!-- 内容 1-->
      <div v-if="activePage == 'mcpIndex'" :key="indexKey" style="height: calc(100vh - 63px);overflow-y: auto;">
        <el-backtop :bottom="100">

          <div style="
        height: 100%;
        width: 100%;
        background-color: #f2f5f6;
        box-shadow: 0 0 6px rgba(0,0,0, .12);
        text-align: center;
        line-height: 40px;
        color: #1989fa;
      ">
            ↑
          </div>
        </el-backtop>

        <div class="indexBac">
          <img alt="" src="/static/view/MCP背景大图.png">
        </div>
        <div class="leftMeun">

          <div class="box"
               style="display: flex;flex-flow: column wrap; gap: 42px; align-items: center;margin-top: 32px;justify-content: center;">
            <p v-for="(item, index) in serverClass" :key="index"
               :class="activedClass == item.value ? 'actived' : ''" style="cursor: pointer;" @click="clickLabel(item)">
              {{
                item.label
              }}
            </p>
          </div>
        </div>
        <div class="search__Czn_C">


          <div class="imgBox">

            <img v-for="(item, index) in titleImgs" :key="index" :src="item" class="imgBoxItem">

          </div>

          <div class="searchContent__SUJdY" data-spm-anchor-id="5176.28197581.d_mcp.i1.24e15a9ei5JKhK">
            <div class="slogan__c4dwV">连接智能，即点即用</div>
            <div style="height: 10px;"></div>
            <div class="slogan__c4dwV">探索全周期MCP服务</div>
            <div class="searchBar__sLzI8">
              <span
                  class="efm_ant-input-affix-wrapper efm_ant-input-outlined input__TOIpB css-var-r3 efm_ant-input-css-var"><span
                  class="efm_ant-input-prefix"></span><input v-model="rParams.serverName" class="efm_ant-input"
                                                             placeholder="搜索你感兴趣的MCP服务" type="text"
                                                             @keyup.enter="searchTable2">
                <svg class="icon" height="20" p-id="2706" t="1748327475342" version="1.1"
                     viewBox="0 0 1024 1024" width="20" xmlns="http://www.w3.org/2000/svg" @click="searchTable2">
                  <path
                      d="M781.7 728l-13.6 13.6-102-102c54.7-61.1 88.3-141.6 88.3-230.1C754.4 218.7 599.7 64 408.9 64S63.4 218.7 63.4 409.5 218.1 755 408.9 755c88.5 0 168.9-33.6 230.1-88.3l102 102-13.6 13.6 177.1 177.1 54.3-54.3L781.7 728z m-680-318.6c0-169.3 137.8-307.1 307.1-307.1s307.1 137.8 307.1 307.1-137.8 307.1-307.1 307.1c-169.3 0.1-307.1-137.7-307.1-307.1z"
                      fill="#707070" p-id="2707"></path>
                </svg>
              </span>

              <el-tooltip class="item" content="创建自定义MCP服务" effect="dark" placement="top">

                <el-button icon="el-icon-plus" @click="addPlus"></el-button>
              </el-tooltip>
            </div>

          </div>
        </div>

        <!-- ------------------ -->

        <div class="official__STV3B">
          <div v-for="(element, num) in list" v-show="list.length > 0" :key="num" class="container">


            <div :class="'boxaaa' + element.tagValue" class="label__ilcza">
              <span>{{ element.label }}</span>
              <span class="count__BZtwX">{{ element.total }} </span>
            </div>

            <!-- list -->
            <div class="list__Hr3u5">
              <div v-for="(item, index) in element.list" :key="index" class="card__v8dLb cardItem__A6pUe"
                   @click="goDetail(item)">
                <div class="efm_ant-flex css-var-r3 efm_ant-flex-align-center" style="gap: 8px;">
                  <img v-if="item.serverImg != null && item.serverImg.length != 0" :src="item.serverImg"
                       class="logoWrap__kzxKL">

                  <img v-else class="logoWrap__kzxKL" src="/static/mcp/默认图标.png">

                  <span
                      class="efm_ant-typography efm_ant-typography-ellipsis efm_ant-typography-ellipsis-single-line title__BG7F4 css-var-r3">{{
                      item.serverName
                    }}</span>
                </div>
                <el-tooltip class="item" effect="dark" placement="top">
                  <div slot="content" style="max-width: 330px;letter-spacing: 1px;font-size: 14px;">
                    {{ item.serverIntro }}
                  </div>
                  <div
                      class="efm_ant-typography efm_ant-typography-ellipsis efm_ant-typography-ellipsis-multiple-line desc__NyQWG market__Ln4vK css-var-r3"
                      style="margin-top: 12px;  -webkit-line-clamp: 2;">
                    {{ item.serverIntro }}
                  </div>
                </el-tooltip>

                <div style="height: 32px;"></div>
                <div style="
  position: absolute;
  bottom: 24px;">
                  <span v-for="(tags, i) in item.serverClasses"
                        :key="i" class="efm_ant-tag efm_ant-tag-borderless spark-tag spark-tag-mauve tag__ruT6p css-var-r3">{{ tags }}</span>
                  <span v-if="item.area"
                        class="efm_ant-tag efm_ant-tag-borderless spark-tag spark-tag-mauve tag__ruT6p css-var-r3">{{ item.area }}</span>

                </div>
              </div>

            </div>
          </div>
        </div>

        <div v-show="listFlag" style="text-align: center;margin: 20px 0;font-style: 12px;">正在加载中 <i
            id="loadingBox" class="el-icon-loading"></i></div>
        <div style="height: 32px;"></div>
        <!-- no data -->
        <div v-if="list.length == 0 && !listFlag" class="emptyCon__jxd5z">

          <div class="spark-empty undefined"
               data-spm-anchor-id="5176.28197581.d_mcp.i11.24e15a9eQW20ho"
               style="">
            <div class="spark-empty-image">
            </div>
            <div class="spark-empty-title" data-spm-anchor-id="5176.28197581.d_mcp.i12.24e15a9eQW20ho">暂无结果</div>
            <div class="spark-empty-description">换个关键词试试吧</div>
          </div>
        </div>



      </div>

      <!-- 内容2 -->
      <mcpServer v-else-if="activePage == 'mcpManage'" @switchPage="onMenu"></mcpServer>
      <myMcp v-else-if="activePage == 'myMcp'" @switchPage="onMenu"></myMcp>
      <mcpTotal v-else-if="activePage == 'mcpTotal'" @switchPage="onMenu"></mcpTotal>
      </transition>


  </div>
</template>
<style lang="less" scoped>
.search__Czn_C {
  width: 700px;
  margin: 0 auto;
  position: relative;
}

.searchBg__dQnPw {
  pointer-events: none;
  width: 100%;
  position: absolute;
  top: -56px;
  transform: scale(1.15);
}

.searchContent__SUJdY {
  z-index: 10;
  flex-direction: column;
  align-items: center;
  padding-top: 62px;
  display: flex;
  position: relative;
}

.slogan__c4dwV {
  color: rgba(38, 36, 76, 0.88);
  font-size: 20px;
  font-weight: 600;
  line-height: 32px;
  margin-bottom: 6px;
  letter-spacing: 1px;
}

.searchBar__sLzI8 {
  align-items: center;
  gap: 8px;
  margin-top: 20px;
  display: flex;
  padding-left: 56px;
  padding-right: 16px;
}


.efm_ant-input {
  font-size: 14px;
  border: none;
  border-radius: 0;
  outline: none;
  background: transparent;
  color: inherit;
  width: 100%;

}


.efm_ant-input-affix-wrapper {
  position: relative;
  display: inline-flex;
  width: 100%;
  min-width: 0;
  align-items: center;
  gap: 8px;
}


.addBtn__W3PD5 {
  cursor: pointer;
  background: #fff;
  border: none;
  border-radius: 100%;
  justify-content: center;
  align-items: center;
  width: 42px;
  height: 42px;
  transition: all .4s;
  display: flex;
}

.addBtn__W3PD5 i {
  color: rgba(38, 36, 76, 0.65);
  font-size: 20px;
  line-height: 1;
}

/* ---------------------------------------------------- */

.official__STV3B {
  max-width: 1464px;
  margin: 28px auto 0;
}

.label__ilcza {
  color: #26244c;

  align-items: center;
  gap: 4px;
  font-weight: 600;
  font-size: 14px;
  line-height: 22px;
  display: flex;
}

.count__BZtwX {
  // color: rgba(38, 36, 76, 0.65);
  margin-left: 12px;
}

/* -----------*--------------------- */
.list__Hr3u5 {
  grid-gap: 20px;
  flex-wrap: wrap;
  // grid-template-columns: repeat(auto-fit, minmax(280px, 0.5fr));
  grid-template-columns: repeat(4, 1fr);

  gap: 40px;
  max-width: 1464px;
  margin: 12px 0 20px;
  display: grid;
}

.card__v8dLb:hover {
  box-shadow: 0px 4px 6px 0px rgba(0, 0, 0, 0.08);
  // border-color: #615ced;
  border-color: #7DACFE;
}

.card__v8dLb {
  height: 181px;
  position: relative;
  border: 1px solid #e6e8ee;
  cursor: pointer;
  // background: radial-gradient(26% 84% at 4% 4%, rgba(221, 220, 229, .3) 0%, rgba(245, 244, 247, 0) 94%), #fff;
  background: #FFF;
  border-radius: 6px;
  padding: 20px;
  transition: all .4s;
}

.efm_ant-flex {
  display: flex;
  margin: 0;
  padding: 0;
}

.efm_ant-flex-align-center {
  align-items: center;
}

.card__v8dLb .desc__NyQWG.market__Ln4vK {
  // min-height: 41px;
}

.card__v8dLb .desc__NyQWG {
  color: rgba(38, 36, 76, 0.45);
  margin: 0;
  font-size: 13px;
  line-height: 20px;
}

.card__v8dLb .tag__ruT6p {
  border: 1px solid #DADFEF;
  height: 20px;
  padding: 4px 12px;
  line-height: 11px;
}

.spark-tag-mauve {
  color: #8E8C99;
  background: rgba(239, 240, 243, 0.8);
}

.efm_ant-tag {
  box-sizing: border-box;
  margin: 0;
  font-size: 12px;
  list-style: none;
  display: inline-block;
  margin-inline-end: 8px;
  padding-inline: calc(8px - 1px);
  white-space: nowrap;

  border-radius: 10px;
  padding: 2px 6px;
  opacity: 1;
  transition: all 0.2s;
  text-align: start;
  position: relative;
}

.card__v8dLb .title__BG7F4 {
  flex: 1;
  font-size: 16px;
  font-weight: 600;
  line-height: 24px;
  letter-spacing: 1px;
}

.logoWrap__kzxKL {
  -o-object-fit: contain;
  object-fit: contain;
  width: 32px;
  height: 32px;
  color: var(--efm_ant-color-text);
  background: #fff;
  border-radius: 6px;
  flex-shrink: 0;
  justify-content: center;
  align-items: center;
  display: flex;
}

/* ************************* */


.spark-empty {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 320px;
  height: 320px;
  overflow: hidden;
  margin: 100px auto 0;

}

.spark-empty-title {
  font-size: 16px;
  font-weight: 500;
  line-height: 24px;
  color: rgba(38, 36, 76, 0.88);
}

.spark-empty-description {
  font-size: 12px;
  line-height: 20px;
  color: rgba(38, 36, 76, 0.88);
  text-align: center;
  width: 100%;
  margin-top: 8px;
}

.efm_ant-typography-ellipsis-multiple-line {
  display: -webkit-box;
  overflow: hidden;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}


.input__TOIpB {
  border: 1px solid #DCDFE6;
  border-radius: 10px;
  width: 330px;
  height: 42px;
  padding-left: 16px;
  padding-right: 16px;
  background-color: #FFFFFF;
}

.el-button {
  padding: 12px;
  border-radius: 10px;
}

.imgBox {
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;

  .imgBoxItem {
    position: relative;
  }

  .imgBoxItem:nth-child(1) {
    height: 32px;
    width: 32px;
    top: calc(77px + 15px);
    left: 34px;
    transform: rotate(-12deg);
  }

  .imgBoxItem:nth-child(2) {
    height: 36px;
    width: 36px;
    top: calc(125px + 15px);
    left: 76px;
    transform: rotate(14deg);
  }

  .imgBoxItem:nth-child(3) {
    height: 32px;
    width: 32px;
    top: calc(9px + 15px);

    left: 70px;
    transform: rotate(21deg);
  }

  .imgBoxItem:nth-child(4) {
    height: 32px;
    width: 32px;

    top: calc(66px + 15px);
    left: 42px;
    transform: rotate(73deg);
  }

  .imgBoxItem:nth-child(5) {
    height: 36px;
    width: 36px;

    top: 135px;
    left: calc(-11px + 460px);
    transform: rotate(20deg);
  }

  .imgBoxItem:nth-child(6) {
    height: 32px;
    width: 32px;

    top: 43px;
    left: calc(-11px + 413px);
    transform: rotate(1deg);
  }

  .imgBoxItem:nth-child(7) {
    height: 36px;
    width: 36px;

    top: 142px;
    left: calc(-5px + 460px);
    transform: rotate(68deg);
  }

  .imgBoxItem:nth-child(8) {
    height: 32px;
    width: 32px;

    top: 70px;
    left: calc(-10px + 422px);
    transform: rotate(27deg)
  }
}


.header {
  background-image: url(/static/homepage/顶部导航栏背景.png);
  background-size: 100% 100%;
  background-repeat: no-repeat;
  margin-bottom: 3px;

  .el-menu {
    li {
      font-size: 16px;
      font-weight: 500;
      font-family: PingFangSC-Medium;
      padding: 0 8px;
    }

    a {
      display: block;
      position: relative;
      padding: 0 24px;

      &.new {
        margin-right: 23px;

        span {
          color: red;
          position: absolute;
          right: -25px;
          top: -10px;
          font-size: 13px;
          font-weight: bold;
        }
      }
    }

    /deep/ .el-menu-item:hover {
      background-color: transparent !important;
      color: #0057ff !important;
    }

    /deep/ .el-submenu__title:hover {
      background-color: transparent !important;
    }

  }


  .user-info {
    display: flex;
    justify-content: flex-end;
    gap: 24px;
    align-items: center;
    padding: 6px 20px 0;

    .dropdown-link {
      cursor: pointer;
      color: #0b264b;
    }

    .iconTextBox {
      width: 24px;
      height: 24px;

      img {
        height: 100%;
        width: 100%;
      }
    }

    .approvalContainer {
      position: relative;
      cursor: pointer;

      .text {
        padding: 0 8px;
        position: absolute;
        right: -18px;
        top: -6px;
        color: #FFF;
        font-size: 10px;
        background-image: url("/static/view/mcp警告背景图.png");

        background-repeat: no-repeat;
        background-size: 100% 100%;
      }
    }

  }

  .logo img {
    width: 283px;
    height: 32px;
  }

  .el-menu--horizontal .el-menu-item a {
    color: #0b264b;
  }
}

.fade-enter-active,
.fade-leave-active {
  transition: all 0.2s;
}

.fade-enter,
.fade-leave-to {
  opacity: 0;
}

.indexBac {
  position: fixed;
  z-index: -1;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;

  img {
    height: 100%;
    width: 100%;
  }
}

.leftMeun {
  position: fixed;
  top: 30%;
  left: 2%;

  img {
    height: 100%;
    width: 100%;
  }

  .box {
    min-width: 156px;
    min-height: 280px;
    background-image: url("/static/view/MCP菜单背景2x.png");
    background-repeat: no-repeat;
    background-size: 100% 100%;

    padding: 42px 0;

    p {
      height: 36px;
      width: 100px;
      text-align: center;
      line-height: 36px;
    }

    .actived {
      color: #FFF;
      background-image: url("/static/view/MCP菜单按钮背景.png");
      background-repeat: no-repeat;
      background-size: 100% 100%;
    }
  }
}

@media screen and (max-width: 1900px) {

  .official__STV3B,
  .list__Hr3u5 {
    max-width: 1000px;
    grid-template-columns: repeat(3, 1fr);

  }
}

@media screen and (max-width: 1500px) {

  .official__STV3B,
  .list__Hr3u5 {
    max-width: 800px;
    grid-template-columns: repeat(2, 1fr);

  }
}
</style>
