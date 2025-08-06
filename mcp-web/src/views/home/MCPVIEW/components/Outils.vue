<script>
import hljs from "highlight.js"
import 'highlight.js/styles/stackoverflow-light.css'
import { js_beautify, css_beautify, html_beautify } from 'js-beautify'
// 工具
export default {
  props: ['info'],

  data() {
    return {
      showList: [],
      ruleForm: {
        name: null,
      },
      rules: {
        name: [
          { required: true, message: '请输入活动名称', trigger: 'blur' },
        ],
      },
      // 验证集合
      allRules: {

      },
      // 所有的api
      apis: [],
      otherApis: [],
      formValues: {},
      resultJson: {},
      runLoading: false,
      apiStatus: {},
      actionKey: "", // 文件上传对应的key
      flieLoading: false,
    }
  },
  methods: {
    showORhidden(item) {
      let index = this.showList.findIndex(i => i == item);
      if (index == -1) {
        this.showList.push(item);
      } else {
        this.showList.splice(index, 1);
      }
    },
    arrHaveValue(item) {
      return this.showList.findIndex(i => i == item) == -1;
    },
    handleChange(file, fileList) {
      this.flieLoading = true;
      console.log(file, fileList);
      if (fileList.length > 1) {
        this.$nextTick(()=>{

          fileList.splice(0,1)
        });
      }
      let data = new FormData();
      data.append("file", file.raw);
      ajax.post(this.$config.gateUrl + '/mcpServer/mcpFileUpload',
        data
        ,
        {
          headers: { "Content-Type": "multipart/form-data" },
        }
      ).then(res => {
        console.log(res);

        this.formValues[this.actionKey[0]][this.actionKey[1]] = res.data;
        file['actionKey'] = [...this.actionKey];
      }).catch(e => {
        console.log(e);


      }).finally(item => {
        this.flieLoading = false;

      });
    },
    async run(item, list) {

      await this.$refs['ruleForm' + item.apiId][0].validate(async (valid) => {
        if (valid) {
          let uploadKey = [];
          for (let i = 0; i < item.serverApiConfigList.length; i++) {
            const element = item.serverApiConfigList[i];
            if (element.dataType == 'file') {
              uploadKey.push(
                'upload' + item.apiId + element.seqId
              );
            }
          }

          let params = structuredClone(this.formValues[item.apiId]);
          let keys = Object.keys(params);

          let key;
          try {
            keys.forEach(item => {
              key = item;
              list.forEach(ele => {

                if (ele.paramName == item && ele.dataType == 'array') {
                  params[item] = JSON.stringify(JSON.parse(params[item]))

                }

              });


            })
          } catch (error) {
            console.log(error);

            this.$message.error(`参数:${key}\t   JSON校验失败`);
            return;
          }


          let headA = {};
          for (let i = 0; i < item.serverApiConfigList.length; i++) {
            const element = item.serverApiConfigList[i];
            if (element.paramType == '1') {
              headA[element.paramName] = element.paramValue
            }

          }

          try {
            debugger
            this.runLoading = true;
            this.apiStatus[item.apiId] = 'ing';
            let resp = await fetch(this.$config.gateUrl + `/mcpServer/mcpServerRun/${this.info.newserverid}/${item.apiName}`, {
              method: 'POST', // 指定请求方法
              headers: {
                'Content-Type': 'application/json',
                Authorization: localStorage.getItem("token"),
                ...headA
              },
              body: JSON.stringify(params)
            });
            let rJson = await resp.text();


            this.$set(this.resultJson, item.apiId, rJson);

            this.$nextTick(() => {
              document.querySelector('#jsonPre' + item.apiId).innerHTML = js_beautify(
                rJson,
                {
                  indent_size: 2,
                  space_in_empty_paren: true
                }
              )
              hljs.highlightElement(document.querySelector('#jsonPre' + item.apiId));

            })
            this.apiStatus[item.apiId] = 'success';

          } catch (error) {
            this.$message.error(error);
            this.apiStatus[item.apiId] = 'error';

          } finally {
            this.runLoading = false;

          }



        } else {

          return false;
        }





      });



    },
    getRule(item) {
      if (item.isRequired == '1') {
        return { required: true, message: '请输入 ' + item.paramName, trigger: 'blur' }
      } else {
        return {}
      }
    },
    getInputType(api) {
      if (api.paramType == '3') {



        let item = api.dataType.toLowerCase();
        switch (item) {
          case "int":
            return 'number'
            break;

          case "string":
            return 'text'

          default:
            return "textarea"
        }
      }
    },
    showConfig() {
      let _ = this;
      return _.info.serverClass != null && _.info.serverClass.length != 0 && _.info.serverClass.indexOf('01') != -1;
    },
    /**
     * @param {字符串} a 
     * 该file所对应的字段所在的对象位置
     * @param {字符串} b 
     * ?
     * @param {字符串} c
     *  file的ref名
     * 
     */
    selectFile(a, b ,c) {
      this.actionKey = [a, b , c];

    },
    /**
     * @param {字符串} a 
     * 该file所对应的字段所在的对象位置
     * @param {字符串} b 
     * ?
     */
    removeFile(a, b, target) {

      this.formValues[a.actionKey[0]][a.actionKey[1]] = "";

    }
  },
  created() {

    // paramtype == 3 是要渲染的参数
    let apis = structuredClone(this.info.apiInfos);

    let allRules = {};
    // form绑定的值
    let formValues = {};

    for (let i = 0; i < apis.length; i++) {
      const element = apis[i];
      const serverApiConfigList = element.serverApiConfigList;

      formValues[element.apiId] = {

      }
      allRules[element.apiId] = {

      }
      // 创建这个元素的验证对象
      for (let j = 0; j < serverApiConfigList.length; j++) {
        const serverApiConfig = serverApiConfigList[j];
        // serverApiConfig.relValue = '';
        if (serverApiConfig.paramType == '3') {
          if (serverApiConfig.isRequired == '1') {
            allRules[element.apiId][serverApiConfig.paramName] = [
              { required: true, message: '请输入' + serverApiConfig.paramName, trigger: 'blur' },
            ]
            formValues[element.apiId][serverApiConfig.paramName] = "";

          }
        }


      }









    }


    this.$set(this, 'apis', apis);
    this.$set(this, 'allRules', allRules);
    this.$set(this, 'formValues', formValues);



  }

}
</script>
<template>

  <div style="margin-top: 28px;">
    <!-- 第一层 -->
    <div class="box" v-for="(item, index) in apis" :key="index">
      <div class="title" @click="showORhidden(item)">
        <span>
          <span>{{ item.apiName }}</span>
        </span>

        <i class="el-icon-arrow-down" v-if="arrHaveValue(item)"> </i>
        <i class="el-icon-arrow-up" v-else> </i>
      </div>
      <div v-if="!arrHaveValue(item)" style="margin-top: 8px;">
        <div class="toolDescription__qvZd9">{{ item.apiDescribe }}</div>
        <div class="sp48" v-if="showConfig()"></div>

        <div class="efm_ant-col efm_ant-form-item-label css-var-r3">
          <el-form :model="formValues[item.apiId]" :ref="'ruleForm' + item.apiId" :att="'ruleForm' + item.apiId"
            label-width="100px" :rules="allRules[item.apiId]" class="demo-ruleForm">
            <!-- 第二层 -->
            <div v-for="api in item.serverApiConfigList" :key="api.seqId" v-show="api.paramType == '3'">
              <label for="address" class="efm_ant-form-item-required-mark-optional" title="">
                <div>
                  <div class="formLabel__vUySF">
                    <div>{{ api.paramName }}</div>
                    <div class="formLabelType__xY91R"> ({{ api.dataType }})</div><span class="required__ZcFYj"
                      v-if="api.isRequired == '1'">*</span>
                  </div>
                  <div class="formDescription__q5IkN">{{ api.description }} </div>
                </div>
              </label>
              <el-form-item :prop="`${api.paramName}`" label-width="0">



                <el-upload v-if="api.dataType == 'file'" class="upload" :ref="'upload' + item.apiId + api.seqId"
                  action="#" :auto-upload="false" :on-change="handleChange" :multiline="false" 
                  :on-remove="removeFile">
                  <el-button slot="trigger" size="small" type="primary" @click="selectFile(item.apiId, api.paramName ,'upload' + item.apiId + api.seqId )"
                    :disabled="flieLoading">选取文件<i
            class="el-icon-loading" style="margin-left: 4px;" v-show="flieLoading"></i></el-button>
                </el-upload>



                <el-input :placeholder="api.exampleValue" :rows="2" :type="getInputType(api)" v-else
                  v-model="formValues[item.apiId][api.paramName]"></el-input>
              </el-form-item>
            </div>
          </el-form>
        </div>
        <el-button @click="run(item, item.serverApiConfigList)" :disabled="runLoading"
          v-if="showConfig()">运行</el-button>
        <span style="display: inline-block;width: 36px;height: 1px;font-size: 14px;"></span>
        <span v-if="apiStatus[item.apiId] == 'ing'" style="color: #ff9900;font-size: 14px;"> 运行中 <i
            class="el-icon-loading"></i></span>
        <span v-else-if="apiStatus[item.apiId] == 'success'" style="color: #19be6b;font-size: 14px;"> 操作成功 </span>
        <span v-else-if="apiStatus[item.apiId] == 'error'" style="color: #ed4014;font-size: 14px;"> 操作失败</span>

        <pre v-loading="runLoading" class="language-json"
          v-if="resultJson[item.apiId] != null && resultJson[item.apiId].length != 0">
                <code :id="'jsonPre' + item.apiId">
                  <!-- {{ info.serverJson }} -->
                </code>
               </pre>
      </div>
    </div>
  </div>
</template>
<style lang="less" scoped>
.box {
  background-color: #cdd0dc26;
  padding: 20px;
  border-radius: 6px;
  margin-bottom: 48px;
}

.title {
  display: flex;
  align-items: center;
  justify-content: space-between;
  cursor: pointer;

  &>span {
    display: flex;
    align-items: center;
    gap: 20px;

    color: rgba(38, 36, 76, 0.88);
    margin-right: 8px;
    font-size: 14px;
    font-weight: 600;
  }
}

.toolDescription__qvZd9 {
  color: rgba(38, 36, 76, 0.45);
  margin-top: 4px;
  font-size: 14px;
  line-height: 24px;
  margin-top: 16px;
}

.efm_ant-form-item-label {
  padding: 0 0 8px;
  margin: 0px;
  white-space: initial;
  text-align: start;
}

.formLabel__vUySF {
  height: 20px;
  color: rgba(38, 36, 76, 0.88);
  align-items: center;
  gap: 4px;
  font-size: 13px;
  font-weight: 500;
  display: flex;
}

.formLabelType__xY91R {
  color: rgba(38, 36, 76, 0.45);
  font-weight: 400;
}

.required__ZcFYj {
  color: #ff4d4f;
  margin-top: 5px;
  line-height: 1;
}

.formDescription__q5IkN {
  color: rgba(38, 36, 76, 0.45);
  margin: 12px 0;
  font-size: 12px;
  line-height: 20px;

}

/deep/.el-input__inner {
  height: 32px;
}
/deep/.el-upload-list__item {
  // transition:all 10s;
  margin-top: 10px;
  // margin-top: 0 !important;
  // transform: translateY(0) !important;

}
/deep/.el-list-leave-active {
  
  transform: translateY(0) !important;
  opacity: 0;
  position: absolute;

  
}
</style>