<template>
  <page-container backgroundColor="#F5F7FB" padding="10px" :showHeader="false">
    <div class="case-body">
      <div class="case-box">
        <div class="backBtn__srMWH efm_ant-app">
          <span @click="goDetail"
            ><i class="el-icon-back"></i>&nbsp;&nbsp;&nbsp;<u>返回MCP广场</u>
          </span>

          <el-button
            type="primary"
            size="small"
            @click="editRow(null)"
            style="font-size: 16px"
            >创建新MCP</el-button
          >
        </div>
        <!-- <p class="backimg" @click="goDetail()"> <img src="/static/newhome/back.png" alt="">  返回MCP广场</p> -->
        <p class="title">管理所有的MCP服务，查看审核状态和使用情况</p>

        <!-- tag区 -->
        <!-- new -->
        <div class="tagBox">
          <el-tabs v-model="serverStatus" @tab-click="searchType">
            <el-tab-pane
              v-for="(item, index) in serverStatusList"
              :key="index"
              :label="item"
              :name="item"
            >
            </el-tab-pane>
          </el-tabs>
        </div>
        <!-- old -->
        <div class="case-box-add" v-if="false">
          <span
            v-for="(item, index) in serverStatusList"
            :key="index"
            @click="searchType(item)"
            :class="item == serverStatus ? 'select' : ''"
            >{{ item }}</span
          >
        </div>
        <div class="case-box-table">
          <div class="case-box-table-box">
            <div class="listCard" v-loading="loading">
              <div
                class="cardContainer"
                @click="toDetail(item)"
                v-for="(item, index) in tableData"
                :key="index"
              >
                <p class="tableTitle">
                  <span>{{ item.serverName }}</span>
                  <!-- <span class="rightStatus1"> -->
                  <span
                    class="rightStatus1"
                    :class="
                      item.serverStatus == '待审核'
                        ? 'waring-tag'
                        : item.serverStatus == '已发布'
                        ? 'success-tag'
                        : ''
                    "
                  >
                    <div class="raDiv">
                      <div
                        style="
                          width: 7px;
                          height: 7px;
                          border: 1px solid #fff;
                          border-radius: 3px;
                          background-color: #fff0;
                        "
                      ></div>
                    </div>
                    <span>{{ item.serverStatus }}</span>
                  </span>
                </p>
                <div class="tableTitle2">
                  <p>
                    <span class="lebel">版本：</span
                    ><span class="value">{{ item.serverVersion }}</span>
                  </p>
                  <p>
                    <span class="lebel">API：</span
                    ><span class="value">{{
                      item.apiNum ? item.apiNum : "0"
                    }}</span>
                    个
                  </p>
                </div>
                <div class="desc">
                  <el-tooltip class="item" effect="dark" placement="top">
                    <div
                      slot="content"
                      style="
                        max-width: 330px;
                        letter-spacing: 1px;
                        font-size: 14px;
                      "
                    >
                      {{ item.serverIntro }}
                    </div>

                    <p>
                      {{ item.serverIntro }}
                    </p>
                  </el-tooltip>
                </div>
                <div style="height: 15px"></div>
                <div class="bottom">
                  <p>
                    <img
                      src="/static/view/mcp分类2x.png"
                      alt=""
                      srcset=""
                    /><span>分类: </span
                    ><span>{{ idtoname(item.serverClass) }}</span>
                  </p>
                </div>
                <div class="bottom bottomTime">
                  <p>
                    <img
                      src="/static/view/MCP创建时间2x.png"
                      alt=""
                      srcset=""
                    /><span>创建时间: </span><span>{{ item.createTime }}</span>
                  </p>
                  <!--
                  <span class="imgs"> 
                    <img src="/static/mcp/审核通过.svg"  title="审核通过" alt="审核通过" v-if="item.serverStatus == '待审核'"
                        @click.stop="shenheyesMcp(item)">
                    <img src="/static/mcp/审核驳回.svg"  title="审核驳回" alt="审核驳回" v-if="item.serverStatus == '待审核'"
                        @click.stop="shenhenoMcp(item)">
                    <img src="/static/mcp/删除.svg"  title="删除" alt="删除"
                        @click.stop="deleteMcp(item)">
                  </span>
                --></div>
                <div
                  class="bottom-hover"
                  v-if="
                    (item.serverStatus == '已创建' ||
                    item.serverStatus == '已驳回') && item.accessType != 'MCP'
                  "
                >
                  <span
                    v-if="!item.serverJson"
                    @click.stop="startMcpServer(item)"
                    >启动服务</span
                  >
                  <span v-if="item.serverJson" @click.stop="mcpStop(item)"
                    >停止服务</span
                  >
                  <span @click.stop="editRow(item)">编辑</span>
                  <el-dropdown placement="right">
                    <span class="el-dropdown-link" @click.stop="">
                      <i class="el-icon-more"></i>
                    </span>
                    <el-dropdown-menu slot="dropdown">
                      <el-dropdown-item @click.native="deleteMcp(item)"
                        >删除</el-dropdown-item
                      >
                    </el-dropdown-menu>
                  </el-dropdown>
                </div>
                <div
                  class="bottom-hover"
                  v-if="
                    (item.serverStatus == '已创建' ||
                    item.serverStatus == '已驳回') && item.accessType == 'MCP'
                  "
                >
                  <span @click.stop="editRow(item)">编辑</span>
                  <span @click.stop="deleteMcp(item)">删除</span>
                </div>
                <div class="bottom-hover" v-if="item.serverStatus == '待审核'">
                  <span @click.stop="shenheyesMcp(item)">审核通过</span>
                  <span @click.stop="shenhenoMcp(item)">审核驳回</span>
                  <el-dropdown placement="right">
                    <span class="el-dropdown-link" @click.stop="">
                      <i class="el-icon-more"></i>
                    </span>
                    <el-dropdown-menu slot="dropdown">
                      <el-dropdown-item
                        v-if="!item.serverJson && item.accessType != 'MCP'"
                        @click.native="startMcpServer(item)"
                        >启动服务</el-dropdown-item
                      >
                      <el-dropdown-item
                        v-if="item.serverJson && item.accessType != 'MCP'"
                        @click.native="mcpStop(item)"
                        >停止服务</el-dropdown-item
                      >
                      <el-dropdown-item @click.native="editRow(item)"
                        >编辑</el-dropdown-item
                      >
                      <el-dropdown-item @click.native="deleteMcp(item)"
                        >删除</el-dropdown-item
                      >
                    </el-dropdown-menu>
                  </el-dropdown>
                </div>
                <div class="bottom-hover" v-if="item.serverStatus == '已发布'">
                  <span @click.stop="newStop(item)">下架</span>
                  <span @click.stop="deleteMcp(item)">删除</span>
                </div>
                <div
                  class="bottom-hover"
                  v-if="
                    item.serverStatus == '已下架' && item.accessType != 'MCP'
                  "
                >
                  <span
                    v-if="!item.serverJson"
                    @click.stop="startMcpServer(item)"
                    >启动服务</span
                  >
                  <span v-if="item.serverJson" @click.stop="mcpStop(item)"
                    >停止服务</span
                  >
                  <span @click.stop="newStart(item)">发布</span>
                  <el-dropdown placement="right">
                    <span class="el-dropdown-link" @click.stop="">
                      <i class="el-icon-more"></i>
                    </span>
                    <el-dropdown-menu slot="dropdown">
                      <el-dropdown-item @click.native="editRow(item)"
                        >编辑</el-dropdown-item
                      >
                      <el-dropdown-item @click.native="deleteMcp(item)"
                        >删除</el-dropdown-item
                      >
                    </el-dropdown-menu>
                  </el-dropdown>
                </div>
                <div
                  class="bottom-hover"
                  v-if="
                    item.serverStatus == '已下架' && item.accessType == 'MCP'
                  "
                >
                  <span @click.stop="newStart(item)">发布</span>
                  <span @click.stop="editRow(item)">编辑</span>
                  <el-dropdown placement="right">
                    <span class="el-dropdown-link" @click.stop="">
                      <i class="el-icon-more"></i>
                    </span>
                    <el-dropdown-menu slot="dropdown">
                      <el-dropdown-item @click.native="deleteMcp(item)"
                        >删除</el-dropdown-item
                      >
                    </el-dropdown-menu>
                  </el-dropdown>
                </div>
              </div>

              <!--
            <el-card v-if="false" shadow="hover" @click.native="toDetail(item)" v-for="(item, index) in tableData"
              :key="index">
              <div class="table-title">
                {{ item.serverName }}
                <div class="buttons">
                  <span class="status-span"
                    :class="item.serverStatus == '待审核' ? 'waring-tag' : item.serverStatus == '已发布' ? 'success-tag' : ''">
                    {{ item.serverStatus }}
                  </span>
                  <img src="/static/newhome/edit.png" title="编辑" alt="编辑" @click.stop="editRow(item)">
                  <img src="/static/newhome/shangjia.png" v-if="item.serverStatus !== '已发布'" title="上架" alt="删除"
                    @click.stop="startMcpServer(item)">
                  <img src="/static/newhome/xiajia.png" v-else title="下架" alt="" @click.stop="mcpStop(item)">

                </div>
              </div>
              <div class="tabs">
                <span>版本：{{ item.serverVersion }}</span>
                <span>分类：{{ idtoname(item.serverClass) }}</span>
                <span>创建时间：{{ item.createTime }}</span>
                <span v-if="item.apiInfos"><i class="el-icon-set-up"></i> {{ item.apiInfos.length }}个api</span>
              </div>
              <div class="jianjie">{{ item.serverIntro }}</div>
            </el-card>
          -->
            </div>
          </div>
          <el-pagination
            style="display: inline-block; float: right; height: 40px"
            background
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="currentPage"
            :page-sizes="[10, 20, 30, 40]"
            :page-size="pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="pagetotal"
          >
          </el-pagination>

          <span
            v-if="pagetotal != 0"
            style="
              float: right;
              position: relative;
              top: 6px;
              font-size: 13px;
              color: #606266;
            "
            >当前显示 {{ getCurrentText() }} 条 ,
          </span>
        </div>
      </div>
    </div>
  </page-container>
</template>

<script>
import editMcpDialog from "./dialog/editMcpDialog.vue";
export default {
  components: {},
  data() {
    return {
      loading: false,
      tableData: [],
      currentPage: 1,
      pageSize: 10,
      pagetotal: 0,
      serverClassList: [],
      serverStatus: "全部",
      serverStatusList: [
        "全部",
        "已创建",
        "待审核",
        "已发布",
        "已驳回",
        "已下架",
      ],
    };
  },
  created() {
    let magne = window.sessionStorage.getItem("magne"); //是否未待审核
    if (magne == "待审核") {
      this.serverStatus = magne;
    }

    // 获取当前 URL
    let currentUrl = window.location.href;
    let i = currentUrl.indexOf("serverStatus");
    let nstr;
    console.log(i);
    if (i !== -1) {
      this.serverStatus = "待审核";
    }
    this.getapilist();
    this.init();
  },
  methods: {
    init() {
      this.loading = true;
      ajax
        .post(`${this.$config.gateUrl}/mcpServer/queryMcpServerInfoList`, {
          index: this.currentPage,
          pageSize: this.pageSize,
          serverName: "",
          serverClass: "全部",
          serverStatus: this.serverStatus,
          userId: null,
        })
        .then((r) => {
          if (r.code == 1000) {
            this.tableData = r.data.records;
            this.pagetotal = r.data.total;
          }
        })
        .finally(() => {
          this.loading = false;
        });
      // this.tableData = [
      //   {
      //     name: "MCP服务1",
      //     id:'1'
      //   },
      //   {
      //     name: "MCP服务1",
      //     id:'1'
      //   },
      //   {
      //     name: "MCP服务1",
      //     id:'1'
      //   },
      //   {
      //     name: "MCP服务1",
      //     id:'1'
      //   },
      //   {
      //     name: "MCP服务1",
      //     id:'1'
      //   }
      // ];
      //  this.loading = false;
    },
    goDetail() {
      this.$emit("switchPage", "mcpIndex");
    },
    getapilist() {
      ajax
        .get(`${this.$config.gateUrl}/mcpServer/getServerClass`, {})
        .then((r) => {
          this.serverClassList = r.data;
        });
    },

    editRow(row) {
      console.log(this.user, "aaaaa");
      this.$dialog(editMcpDialog, {
        seqId: row ? row.seqId : "",
        pageType: 2,
        usermsg: this.user,
        createType: row ? (row.accessType ? row.accessType : "API") : "API",
      }).then((r) => {
        this.init();
      });
    },
    deleteMcp(item) {
      this.$confirm("确认要删除吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        ajax
          .get(`${this.$config.gateUrl}/mcpServer/deleteMcp`, {
            mcpId: item.seqId,
          })
          .then((r) => {
            if (r.code == 1000) {
              this.$message.success(`删除成功`);
              this.init();
            }
          });
      });
    },
    searchType(type, event) {
      this.serverStatus = type.label;
      this.currentPage = 1;
      debugger;
      this.init();
    },
    idtoname(str) {
      const idArray = str.split(",");

      // 使用 filter() 方法筛选出符合 id 数组中的 id 的对象，然后用 map() 方法获取对应的 name 属性
      const filteredNames = this.serverClassList
        .filter((obj) => idArray.includes(obj.value))
        .map((obj) => obj.label);

      // 使用 join() 方法将 name 组合成字符串，以 , 分隔
      const result = filteredNames.join(", ");
      return result;
    },
    toDetail(item) {
      window.open(
        window.location.origin + `/#/mcpview/mcpDetail?mcpId=${item.seqId}`
      );
    },
    // changestatus(row,item1) {

    // },
    // changeStatus(row,item1){
    //   ajax
    //     .post(`${this.$config.gateUrl}/mcpServer/updateMcpStatus`, {
    //       seqId: row.seqId,
    //       serverStatus: item1,
    //     })
    //     .then((r) => {
    //       if (r.code == 1000) {
    //         row.serverStatus = item1;
    //         this.$message.success(`更改状态成功`);
    //         this.init();
    //       }
    //     });
    // },
    startMcpServer(item) {
      ajax
        .post(`${this.$config.gateUrl}/mcpServer/startMcpServer`, {
          seqId: item.seqId,
        })
        .then((r) => {
          if (r.code == 1000) {
            this.$message.success(`启动server成功`);
            this.init();
          }
        });
    },
    mcpStop(item) {
      ajax
        .get(`${this.$config.gateUrl}/mcpServer/mcpStop`, {
          mcpId: item.seqId,
        })
        .then((r) => {
          if (r.code == 1000) {
            this.$message.success(`停止server成功`);
            this.init();
          }
        });
    },
    newStart(item) {
      if (item.accessType != "MCP") {
        ajax
          .post(`${this.$config.gateUrl}/mcpServer/isStartupJudgment`, {
            seqId: item.seqId,
          })
          .then((r) => {
            if (r.data) {
              ajax
                .post(`${this.$config.gateUrl}/mcpServer/serverPublish`, {
                  seqId: item.seqId,
                })
                .then((r) => {
                  if (r.code == 1000) {
                    this.$message.success(`发布成功`);
                    this.init();
                  }
                });
            } else {
              this.$message.error(`请先启动server`);
            }
          });
      } else {
        ajax
          .post(`${this.$config.gateUrl}/mcpServer/serverPublish`, {
            seqId: item.seqId,
          })
          .then((r) => {
            if (r.code == 1000) {
              this.$message.success(`发布成功`);
              this.init();
            }
          });
      }
    },
    newStop(item) {
      if (item.accessType != "MCP") {
        ajax
          .get(`${this.$config.gateUrl}/mcpServer/mcpStop`, {
            mcpId: item.seqId,
          })
          .then((r) => {
            if (r.code == 1000) {
              this.$message.success(`停止server成功`);
              ajax
                .post(`${this.$config.gateUrl}/mcpServer/serverOffline`, {
                  seqId: item.seqId,
                })
                .then((r) => {
                  if (r.code == 1000) {
                    this.$message.success(`下架成功`);
                    this.init();
                  }
                });
            }
          });
      } else {
        ajax
          .post(`${this.$config.gateUrl}/mcpServer/serverOffline`, {
            seqId: item.seqId,
          })
          .then((r) => {
            if (r.code == 1000) {
              this.$message.success(`下架成功`);
              this.init();
            }
          });
      }
    },
    shenheyesMcp(item) {
      if (item.accessType != "MCP") {
        ajax
          .post(`${this.$config.gateUrl}/mcpServer/isStartupJudgment`, {
            seqId: item.seqId,
          })
          .then((r) => {
            if (r.data) {
              ajax
                .post(`${this.$config.gateUrl}/mcpServer/reviewPassed`, {
                  seqId: item.seqId,
                })
                .then((r) => {
                  if (r.code == 1000) {
                    this.$message.success(`审核通过`);
                    this.init();
                  }
                });
            } else {
              this.$message.error(`请先启动server`);
            }
          });
      } else {
        ajax
          .post(`${this.$config.gateUrl}/mcpServer/reviewPassed`, {
            seqId: item.seqId,
          })
          .then((r) => {
            if (r.code == 1000) {
              this.$message.success(`审核通过`);
              this.init();
            }
          });
      }
    },
    shenhenoMcp(item) {
      ajax
        .post(`${this.$config.gateUrl}/mcpServer/reviewRejected`, {
          seqId: item.seqId,
        })
        .then((r) => {
          if (r.code == 1000) {
            this.$message.success(`审核已驳回`);
            this.init();
          }
        });
    },
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
      this.pageSize = val;
      this.init();
    },
    handleCurrentChange(val) {
      this.currentPage = val;
      console.log(`当前页: ${val}`);
      this.init();
    },
    getCurrentText() {
      let num = 0;
      let num2 = 0;
      num = (this.currentPage - 1) * this.pageSize + 1;
      num2 = (this.currentPage - 1 + 1) * this.pageSize;
      if (num2 > this.pagetotal) {
        num2 = this.pagetotal;
      }

      return num + " - " + num2;
    },
  },
};
</script>

<style lang="less" scoped>
.case-body-add {
  // text-align: right;
  padding-top: 10px;
  // padding-right: 40px;
}

.case-body {
  // width: calc(100% - 30px);
  background: #ffffff;
  border-radius: 2px;
  // min-height: 900px;
  padding: 0 15px;
  overflow: auto;
  padding-top: 10px;
}

.case-box {
  .backimg {
    img {
      width: 15px;
      height: 15px;
    }

    color: rgb(70, 144, 249);
    text-decoration: underline;
    display: inline-block;

    &:hover {
      cursor: pointer;
    }
  }

  .title {
    color: rgb(118, 118, 119);
    margin: 12px 0;
    font-weight: 400;
    font-size: 14px;
    color: #303133;
  }

  .case-box-add {
    margin-bottom: 16px;
    height: 30px;
    display: flex;
    align-items: center;

    span {
      font-size: 14px;
      color: rgb(118, 118, 119);
      margin-right: 20px;
      cursor: pointer;
      border: 1px solid rgb(118, 118, 119);
      padding: 4px 10px;
      border-radius: 3px;
    }

    .select {
      background: rgb(52, 152, 219);
      color: #ffffff;
      border-color: rgb(52, 152, 219);
    }

    .el-button {
      position: absolute;
      right: 20px;
    }
  }

  .case-box-table {
    height: calc(100vh - 265px);

    .case-box-table-box {
      height: calc(100vh - 265px);
      overflow: auto;
      overflow-y: scroll;

      .el-card {
        font-size: 14px;
        color: rgb(118, 118, 119);
        margin-bottom: 10px;
        cursor: pointer;

        .table-title {
          font-size: 16px;
          color: black;
          font-weight: 500;

          .buttons {
            float: right;
            margin-right: 20px;

            .status-span {
              margin-right: 50px;
            }

            .waring-tag {
              color: rgb(247, 203, 114);
            }

            .success-tag {
              color: rgb(110, 217, 146);
            }

            img {
              width: 20px;
              height: 20px;
              cursor: pointer;
              margin-right: 10px;
            }
          }

          .jianjie {
            padding-right: 230px;
          }
        }

        .tabs {
          margin: 5px 0;

          span {
            margin-right: 20px;
          }
        }
      }
    }
  }
}

.backBtn__srMWH {
  cursor: pointer;
  color: rgba(38, 36, 76, 0.88);
  align-items: center;
  gap: 8px;
  display: flex;
  justify-content: space-between;
}

.efm_ant-app {
  color: rgba(38, 36, 76, 0.88);
  font-size: 16px;
  line-height: 1.5;
  color: rgb(70, 144, 249);
}

.tagBox {
  /deep/.el-tabs__item {
    padding: 5px 44px !important;
    height: 32px;
    line-height: 20px;
  }

  /deep/.el-tabs__nav-wrap::after {
    background-color: #2b78fe;
    height: 1px;
  }

  /deep/.el-tabs__active-bar {
    display: none;
  }

  /deep/.is-active {
    background: #2b78fe;
    border-radius: 8px 8px 0px 0px;
    border: 1px solid #2b78fe;
    color: #ffffff;
  }
}

.listCard {
  min-height: 100px;
  grid-gap: 20px;
  flex-wrap: wrap;
  // grid-template-columns: repeat(auto-fit, minmax(380px, 0.5fr));
  grid-template-columns: 1fr 1fr 1fr 1fr;
  gap: 20px;
  // max-width: ;
  margin: 12px 0 20px;
  display: grid;
  cursor: pointer;

  .cardContainer {
    padding: 26px;
    background-image: url("/static/view/MCP管理卡片背景.png");
    position: relative;
    background-repeat: no-repeat;
    background-size: 100% 100%;
    display: flex;
    flex-flow: column wrap;
    gap: 8px;

    .tableTitle {
      display: flex;
      justify-content: space-between;
      align-items: center;

      font-size: 18px;

      > span:nth-child(1) {
        font-weight: 600;
        color: #303133;
      }

      .rightStatus1 {
        color: #000;
        display: flex;
        align-items: center;
        gap: 10px;

        img {
          width: 16px;
          height: 16px;
          margin: 0 4px;
        }

        .bigimg {
          width: 20px;
          height: 20px;
          position: relative;
          top: 2px;
        }

        span {
          font-size: 16px;
        }

        .raDiv {
          width: 10px;
          height: 10px;
          display: flex;
          background: #000;
          border-radius: 5px;
          align-items: center;
          justify-content: center;
        }
      }

      .waring-tag {
        color: #ffa000;

        .raDiv {
          background: #ffa000;
        }
      }

      .success-tag {
        color: #16c90f;

        .raDiv {
          background: #16c90f;
        }
      }
    }

    .tableTitle2 {
      display: flex;
      align-items: center;
      gap: 32px;
      font-weight: 400;

      font-size: 15px;

      p {
        display: inline-block;
        min-width: 80px;
      }

      .lebel {
      }

      .value {
        font-weight: 600;
        font-size: 15px;
        color: #2b78fe;
      }
    }

    .desc {
      font-size: 15px;
      color: #30313399;
      flex: 1;

      p {
        display: -webkit-box;
        overflow: hidden;
        -webkit-line-clamp: 3;
        -webkit-box-orient: vertical;
      }
    }

    .bottom {
      display: flex;
      align-items: center;
      flex-flow: row wrap;
      font-size: 15px;
      justify-content: space-between;
      transition: opacity 0.3s;

      p {
        display: flex;
        align-items: center;
        gap: 4px;

        img {
          width: 16px;
          height: 16px;
        }
      }

      p:nth-child(1) {
        margin-right: 16px;
      }

      .imgs {
        img {
          width: 16px;
          height: 16px;
          margin: 0 4px;
        }
      }
    }

    .bottom-hover {
      // display: none;
      opacity: 0;
      position: absolute;
      bottom: 21px;
      width: 90%;
      background: #fff3;
      transition: opacity 0.5s;

      > span {
        display: inline-block;
        width: 100px;
        height: 31px;
        line-height: 28px;
        border-radius: 4px 4px 4px 4px;
        cursor: pointer;
        margin-right: 17px;
        color: #a8acbc;
        border: 1px solid #d5dbef;
        text-align: center;

        font-weight: 600;
        font-size: 12px;
      }

      > span:nth-child(1) {
        background: #2b78fe;
        color: #ffffff;
        border: 1px solid #2b78fe;
      }

      .el-dropdown-link {
        cursor: pointer;
        text-align: center;
        border: solid 1px #d5dbef;
        width: 24px !important;
        text-align: center;
        display: inline-block;
      }
    }
  }

  .cardContainer:hover {
    // box-shadow: 0px 4px 6px 0px rgba(0, 0, 0, 0.08);
    // border-color: #615ced; // border-color: #7DACFE;
    .bottom-hover {
      opacity: 1;
    }

    .bottomTime {
      opacity: 0;
    }
  }
}
</style>