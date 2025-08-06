<script>
import Content from './components/Content.vue';
export default {
  data() {
    return {
      toolTab: 1,
      info: {},
      fromPage: null,
      loading: true,
    }
  },
  components: {
    overview: () => import('./components/Overview.vue'),
    outils: () => import('./components/Outils.vue'),
    contentView: Content,
  },
  methods: {
    backPage() {
      this.$router.back();
    },
    async queryData() {

      try {
        let resp = await ajax
          .get(`${this.$config.gateUrl}/mcpServer/detailMcp?mcpId=` + this.$route.query.mcpId);

        this.info = resp.data;
      } catch (error) {
        console.log(error);

      } finally {
        this.loading = false;
      }

    },
    updateFromPage(from, to) {

      this.fromPage = from.path;

    },
    showConfig(){
      let _ = this;
      return _.info.serverClass !=null && _.info.serverClass.length !=0 && _.info.serverClass.indexOf('03') == -1 ;
    },
    showTool(){
      let _ = this;
      return _.info.accessType=='API';
    }
  },
  created() {
    this.queryData();


  },
  beforeRouteEnter(to, from, next) {
    next(vm => {
      vm.updateFromPage(from, to);
    })
  },
  mounted() {
    window.scrollTo(0, 0);
  }
}
</script>
<template>
  <div class="mcpHomeDetail__kyKIP" >

    <div class="header__G_SPk">
      <div class="backBtn__srMWH efm_ant-app " v-if="fromPage == '/mcpview/FirstIndex'"><span @click="backPage"><i
            class="el-icon-back"></i>&nbsp;&nbsp;&nbsp;返回广场</span>
      </div>
      <div class="titleWrapper__lM77D">
        <img :src="info.serverImg" v-if="info.serverImg != null && info.serverImg.length > 0" class="logo__D4Zyr">
        <img src="/static/mcp/默认图标.png" v-else class="logo__D4Zyr">
      

        <h1 class="title__QE9Nz">{{ info.serverName }} </h1>
      </div>
      <div class="providerTag__QCt20" v-if="true">
        <div class="description__bEu5P efm_ant-app">{{ info.serverIntro }}</div>
      </div>
    </div>

    <div style="margin-top: 20px">
      <el-radio-group v-model="toolTab" size="medium">
        <el-radio-button :label="1">概览</el-radio-button>
        <el-radio-button :label="3" v-if="showConfig()">配置</el-radio-button>
        <el-radio-button :label="2" v-if="showTool()">工具</el-radio-button>
        <!-- <el-radio-button label="资源" disabled title="敬请期待" v-if="false"></el-radio-button> -->
        <!-- <el-radio-button label="提示词" disabled title="敬请期待" v-if="false"></el-radio-button> -->
      </el-radio-group>
    </div>

    <div style="height: 20px;"></div>
    <transition name="fade" mode="out-in">
      <div v-if="loading" style="height: 300px;" v-loading="loading"></div>
      <overview :info="info" v-else-if="toolTab == 1"></overview>
      <outils :info="info" v-else-if="toolTab == 2"></outils>
      <contentView :info="info" v-else-if="toolTab == 3"></contentView>
    </transition>







  </div>

</template>
<style lang="less" scoped>
* {
  letter-spacing: 1px;
}

.mcpHomeDetail__kyKIP {
  background-color: #f9fafd;
  flex-direction: column;
  width: 100%;
  max-width: 1180px;
  height: 100%;
  margin: 0 auto;
  padding: 40px 0;
  display: flex;
  overflow-y: auto;
}

.efm_ant-app {
  color: rgba(38, 36, 76, 0.88);
  font-size: 14px;
  line-height: 1.5;
}

.header__G_SPk {
  border-bottom: 1px solid #e6e8ee;
  flex-direction: column;
  align-items: flex-start;
  padding-bottom: 24px;
  display: flex;
}

.backBtn__srMWH {
  cursor: pointer;
  color: rgba(38, 36, 76, 0.88);
  align-items: center;
  gap: 8px;
  margin-bottom: 20px;
  display: flex;
}

.titleWrapper__lM77D {
  align-items: center;
  margin-bottom: 8px;
  display: flex;
}

.logo__D4Zyr {
  -o-object-fit: contain;
  object-fit: contain;
  border-radius: 6px;
  width: 40px;
  height: 40px;
  margin-right: 16px;
}

.title__QE9Nz {
  color: #26244c;
  font-size: 24px;
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 16px;
}

.providerTag__QCt20 {
  justify-content: space-between;
  align-items: flex-start;
  width: 100%;
  display: flex;
  margin-top: 16px;
}

.description__bEu5P {
  color: rgba(38, 36, 76, 0.65);
}

.guide__WvH3A {
  letter-spacing: 0;
  color: rgba(38, 36, 76, 0.88);
  cursor: pointer;
  flex-direction: row;
  flex-shrink: 0;
  justify-content: space-between;
  align-items: center;
  gap: 4px;
  padding: 0;
  font-size: 14px;
  font-weight: 400;
  line-height: 24px;
  display: flex;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s;
}

.fade-enter,
.fade-leave-to {
  opacity: 0;
}

/deep/.el-radio-button__inner {
  background-color: #EEEFF4;
  border: none;
  letter-spacing: 0;
}

/deep/.el-radio-button__orig-radio:checked+.el-radio-button__inner {
  background-color: #2B78FE;
}

/deep/.el-radio-button__orig-radio:disabled+.el-radio-button__inner {
  background-color: #F3F4F9;
}

/deep/.el-radio-button:first-child .el-radio-button__inner {
  border: none;
}

/deep/.blockBlue {
  display: inline-block;
  height: 16px;
  width: 6px;
  background-color: #2B78FE;
}

/deep/.ne-text {
  display: flex;
  gap: 10px;
  align-items: center;
  margin-bottom: 48px;
  white-space: pre-wrap;
}

/deep/.sp48 {
  height: 48px;
  width: 48px;
}
</style>