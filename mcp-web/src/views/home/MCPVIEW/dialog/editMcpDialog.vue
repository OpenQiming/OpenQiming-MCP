<template>
  <dialog-container :title="dialogName" :width="getDialogContainerWidth()" @success="onSuccess">
    <el-tabs v-model="activeName">
      <el-tab-pane label="MCP接入" name="second" class="mcp-box">
        <div class="mcp-box">
          <el-form :model="formMcp" :rules="rulesMcp" ref="formMcp" label-width="120px">
            <el-row>
              <el-col :span="12">
                <el-form-item label="MCP名称：" prop="serverName">
                  <el-input v-model="formMcp.serverName" placeholder="如：高德地图服务"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="版本号：" prop="serverVersion">
                  <el-input v-model="formMcp.serverVersion" placeholder="如：1.0.0"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="开放范围：" prop="openRange">
                  <el-select v-model="formMcp.openRange" placeholder="请选择开放范围">
                    <el-option v-for="item in openRangeList" :key="item.value" :label="item.label" :value="item.value">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="所属区域：" prop="area">
                  <el-input v-model="formMcp.area"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-form-item label="图标：" prop="serverImg">
                <el-upload class="avatar-uploader" ref="upload" action="#" :show-file-list="false"
                           :before-upload="beforeUpload" :on-success="handleChangeMCP" :on-change="onChange" :auto-upload="false"
                           :data="addList">
                  <img v-if="formMcp.serverImg" :src="formMcp.serverImg" class="avatar"
                       style="width: 100px; height: 100px" alt />
                  <i v-else class="el-icon-plus avatar-uploader-icon" style="font-size: 25px"></i>
                </el-upload>
              </el-form-item>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="分类：" prop="serverClass">
                  <el-select v-model="formMcp.serverClass" placeholder="请选择分类" @change="classChange()">
                    <el-option v-for="item in serverClassList" :key="item.value" :label="item.label"
                               :value="item.value">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="服务介绍：" prop="serverIntro">
                  <el-input type="textarea" :autosize="{ minRows: 2, maxRows: 2 }" placeholder="详细描述您的MCP服务的功能和用途..."
                            v-model="formMcp.serverIntro">
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="主要特点：" prop="mainFeature">
                  <el-input type="textarea" :autosize="{ minRows: 2, maxRows: 2 }" placeholder="指述服务的核心特点..."
                            v-model="formMcp.mainFeature">
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="使用案例：" prop="useCase">
                  <el-input type="textarea" :autosize="{ minRows: 2, maxRows: 2 }" placeholder="提供典型的使用场景..."
                            v-model="formMcp.useCase">
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="常见问题：" prop="commonQuestion">
                  <el-input type="textarea" :autosize="{ minRows: 2, maxRows: 2 }" placeholder="请列出用户可能关心的问题和解答..."
                            v-model="formMcp.commonQuestion">
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="24">
                <p><span style="color: red;position: relative;top: 2px;">*</span><b>&nbsp;配置：</b> </p>
              </el-col>
              <el-col :span="12">
                <el-form-item label="服务配置：" prop="serverJson" class="editBox">
                  <vue-json-editor v-model="formMcp.serverJson" :showBtns="false" mode="code" lang="zh"
                                   :expanded-on-start="true" />
                </el-form-item>
              </el-col>

              <el-col :span="12">

                <el-form-item label="环境变量配置：" prop="envParams" class="textLeft editBox">



                  <el-card shadow="never" style="" class="envParamsCard">
                    <div style="height: 200px;overflow-y: auto;">
                      <el-row v-for="(item, index) in formMcp.envParams" :key="index" style="margin-bottom: 12px;">
                        <el-col :span="24">
                          <el-form-item :label="item.paramName" prop="name" class="editBox">
                            <div style="display: flex;align-items: center;gap: 4px;">
                              <el-input placeholder="请输入参数值" v-model="item.exampleValue"></el-input>
                              <i class="el-icon-tickets" style="margin-left: 5px" title="编辑"
                                 @click="editenvParams(item, index)"></i>
                              <i class="el-icon-delete" style="margin-left: 5px" title="删除"
                                 @click="deleteEnvParams(index)"></i>

                            </div>
                          </el-form-item>
                        </el-col>
                      </el-row>


                    </div>
                    <el-button style="display: block;margin: 15px auto 0;  " type="primary" size="mini"
                               @click="editenvParams(null, null)">添加</el-button>
                  </el-card>
                </el-form-item>

              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="README：" prop="readme" class="editBox">
                  <template slot="label">

                    <el-popover placement="top-start" title="" width="500" trigger="hover"
                                content="建议填写工具详情，使用协议及帮助用户理解使用 MCP server 的内容；若需本地部署，请尽量提供详细的安装方法">
                      <template slot="reference">
                        <span style="display: flex;gap: 4px;align-items: center;">
                          README： <i class="el-icon-question" style="color: #409eff;cursor: pointer;"></i>
                        </span>

                      </template>

                    </el-popover>
                  </template>

                  <mavon-editor v-model="formMcp.readme" class="formclass" :toolbars="toolbars" :subfield="false"
                                :placeholder="MEPlaceholder"
                                :class="formMcp.readme == null || formMcp.readme.length == 0 ? 'MEClass' : ''"
                                @click="handleMEClick" ref="mdedit"></mavon-editor>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
          <el-dialog :title="envParamstitle" :visible.sync="envParamsVisible" append-to-body width="40%">
            <el-form ref="envParamsForm" :model="addenvParamsObj" :rules="envParamsRules" label-width="120px">
              <el-row>
                <el-col :span="24">
                  <el-form-item label="变量名：" prop="paramName">
                    <el-input placeholder="请输入变量名" v-model="addenvParamsObj.paramName"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24">
                  <el-form-item label="描述：" prop="description">
                    <el-input placeholder="请输入描述" v-model="addenvParamsObj.description"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24">
                  <el-form-item label="详细指引：" prop="supplyNotes">
                    <el-input placeholder="请输入详细指引" v-model="addenvParamsObj.supplyNotes"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24">
                  <el-form-item label="是否必填：" prop="isRequired">
                    <el-switch v-model="addenvParamsObj.isRequired">
                    </el-switch>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24">
                  <el-form-item label="类型：" prop="dataType">
                    <el-select v-model="addenvParamsObj.dataType" placeholder="请选择参数类型">
                      <el-option v-for="item in dataTypeList" :key="item" :label="item" :value="item"></el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
            </el-form>

            <div slot="footer" class="dialog-footer">
              <el-button @click="envParamsVisible = false">取 消</el-button>
              <el-button type="primary" @click="addenvParam()">确 定</el-button>
            </div>
          </el-dialog>
        </div>
      </el-tab-pane>
    </el-tabs>
    <div slot="footer" class="dialog-footer">
      <el-button @click="$emit('cancel')">取 消</el-button>
      <el-button type="primary" @click="onSuccess()" :disabled="loading"><i class="el-icon-loading" v-if="loading"></i>确
        定</el-button>
    </div>
  </dialog-container>
</template>

<script>
import { mavonEditor } from "mavon-editor";
import vueJsonEditor from "vue-json-editor";
import "mavon-editor/dist/css/index.css";
import tips from "./MdText"
export default {
  props: {
    seqId: String,
    createType: String,
    pageType: Number,
    usermsg: Object,
  },
  components: {
    mavonEditor,
    vueJsonEditor,
  },
  data() {
    return {
      envParamstitle: "",
      envParamsVisible: false,
      addIndex: null,
      loading: false,
      addenvParamsObj: {
        paramName: "",
        value: "",
        dataType: "",
        isRequired: true,
        description: "",
        supplyNotes: "",
        exampleValue: "",
      },
      envParamsRules: {
        paramName: [{ required: true, message: "请输入环境变量名称" }],
        dataType: [{ required: true, message: "请选择环境变量类型" }],
      },
      dataTypeList: ["string", "number", "integer", "boolean", "array"],
      dialogName: "",
      activeName: "second",
      newapiIds: "",
      dialogFormVisible: false,
      imageUrl: "",
      imageUrl1: "",
      imageUrl2: "",
      imageUrl3: true,
      addList: {},
      formMcp: {
        serverName: "",
        serverVersion: "",
        serverClass: "",
        serverIntro: "",
        mainFeature: "",
        useCase: "",
        commonQuestion: "",
        serverImg: "",
        openRange: "",
        area: "",
        accessType: "MCP",
        envParams: [],
        serverJson: "",
        readme: "",
      },
      rulesMcp: {
        serverName: [{ required: true, message: "请输入MCP名称" }],
        serverVersion: [{ required: true, message: "请输入版本号" }],
        serverIntro: [{ required: true, message: "请输入服务介绍" }],
        useCase: [{ required: true, message: "请输入主要特点" }],
        mainFeature: [{ required: true, message: "请输入使用案例" }],
        commonQuestion: [{ required: true, message: "请输入常见问题" }],
        serverClass: [{ required: true, message: "请选择服务分类" }],
        openRange: [{ required: true, message: "请选择开放范围" }],
        serverJson: [{ required: true, message: "请输入服务配置" }],
      },
      apiList: [],
      serverClassList: [],
      newapiIdsList: [],
      multipleSelection: [],
      openRangeList: [],
      diaform: {
        apiName: "",
        serverClass: "03",
        interfaceType: "",
      },
      pageNum: 1,
      pageSize: 10,
      pageTotal: 0,
      interfaceTypeList: ["GET", "POST", "PUT", "DELETE", "PATCH"],
      toolbars: {
        bold: true, // 粗体
        italic: true, // 斜体
        header: true, // 标题
        underline: true, // 下划线
        strikethrough: true, // 中划线
        mark: true, // 标记
        superscript: true, // 上角标
        subscript: true, // 下角标
        quote: true, // 引用
        ol: true, // 有序列表
        ul: true, // 无序列表
        link: true, // 链接
        imagelink: false, // 图片链接
        code: false, // code
        table: true, // 表格
        fullscreen: true, // 全屏编辑
        readmodel: true, // 沉浸式阅读
        htmlcode: true, // 展示html源码
        help: true, // 帮助
        /* 1.3.5 */
        undo: true, // 上一步
        redo: true, // 下一步
        trash: true, // 清空
        save: true, // 保存（触发events中的save事件）
        /* 1.4.2 */
        navigation: true, // 导航目录
        /* 2.1.8 */
        alignleft: true, // 左对齐
        aligncenter: true, // 居中
        alignright: true, // 右对齐
        /* 2.2.1 */
        subfield: true, // 单双栏模式
        preview: true, // 预览
      },
      /**
       * @description 编辑器的占位符文本
       * @type {string}
       */
      MEPlaceholder: "",
      /**
       * @description 编辑器的占位符文本
       * @type {string}
       */
      MEPlaceholderText: "",
    };
  },
  created() {
    this.MEPlaceholder = tips;
    this.MEPlaceholderText = this.MEPlaceholder;
    this.init();
    this.getseverclass();
    this.getopenRange();

    this.formMcp.area = this.usermsg.regionName;
  },
  methods: {
    init() {
      if (this.createType == "MCP") {
        this.activeName = "second";
      }
      this.dialogName = this.seqId ? "修改MCP服务" : "创建MCP服务";
      if (this.seqId) {
        this.getDetail();
      } else {
      }
      // this.$refs.form.resetFields();
    },
    editenvParams(envobj, index) {
      this.envParamstitle = envobj ? "修改环境变量" : "添加环境变量";
      if (envobj) {
        this.addenvParamsObj = _.cloneDeep(envobj);
        this.addIndex = index;
      } else {
        this.addenvParamsObj = {
          paramName: "",
          value: "",
          dataType: "",
          isRequired: true,
          description: "",
          supplyNotes: "",
          exampleValue: "",
        };
        this.addIndex = null;
      }
      this.envParamsVisible = true;
      this.$nextTick(() => {
        this.$refs.envParamsForm.clearValidate();
      });


    },
    addenvParam() {
      this.$refs.envParamsForm.validate((valid) => {
        if (valid) {
          console.log(this.addIndex);

          if (this.addIndex != null) {
            this.formMcp.envParams[this.addIndex] = this.addenvParamsObj;
          } else {
            this.formMcp.envParams.push(this.addenvParamsObj);
          }
          this.envParamsVisible = false;
        }
      });
    },
    deleteEnvParams(index) {
      console.log(index);
      this.$confirm(
          "请确认要删除此参数吗？",
          "删除确认",
          {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          }
      )
          .then(() => {
            this.formMcp.envParams.splice(index, 1);
          })
          .catch(() => {
            this.$message({
              type: "info",
              message: "已取消删除",
            });
          });

    },
    add() {
      if (process.env.enviroment == "prodCN") {

      } else {

      }
    },
    edit(row) {
      if (process.env.enviroment == "prodCN") {
      } else {
      }
    },
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
    },
    handleCurrentChange(val) {
      this.pageNum = val;
      this.getapilist();
    },
    getnewapilist() {
      this.pageNum = 1;
      this.getapilist();
    },
    //  handleBeforeUpload(file) {
    //   console.log(file,'file')
    //   const reader = new FileReader();
    //   reader.readAsDataURL(file); // 读取文件内容为DataURL
    //   reader.onload = (e) => {
    //     const base64 = e.target.result; // 获取Base64编码后的字符串
    //     console.log(base64); // 打印或使用Base64字符串
    //     this.form.serverImg = base64;
    //     // 可以在这里将base64字符串发送到服务器或进行其他处理
    //   };
    //   // 返回false阻止自动上传
    //   // return false;
    // },
    //     handleRemove(file) {
    //       console.log('移除了文件:', file);
    //     },
    beforeUpload() {
      const isJPG = file.type === "image/jpeg";
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isJPG) {
        this.$message.error("上传头像图片只能是 JPG 格式!");
      }
      if (!isLt2M) {
        this.$message.error("上传头像图片大小不能超过 2MB!");
      }
      return isJPG && isLt2M;
    },
    onChange(file, fileList) {
      var _this = this;
      var event = event || window.event;
      var file = event.target.files[0];
      var reader = new FileReader();
      //转base64
      reader.onload = function (e) {

        if (_this.activeName == 'first') {
          // _this.form.serverImg = e.target.result; //将图片路径赋值给src
          _this.$set(_this.form, 'serverImg', e.target.result);
        } else if (_this.activeName == 'second') {
          // _this.formMcp.serverImg = e.target.result; //将图片路径赋值给src
          _this.$set(_this.formMcp, 'serverImg', e.target.result);
        }
      };
      reader.readAsDataURL(file);
    },
    handleChange(res, file) {
      this.form.serverImg = URL.createObjectURL(file.raw);
    },
    handleChangeMCP(res, file) {
      this.formMcp.serverImg = URL.createObjectURL(file.raw);
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
      console.log(val, "val");
    },
    getapilist() {
      ajax
          .post(`${this.$config.gateUrl}/mcpServer/apiList`, {
            ...this.diaform,
            pageNum: this.pageNum,
            pageSize: this.pageSize,
          })
          .then((r) => {
            this.apiList = r.data.records;
            this.pageTotal = r.data.total;
            if (this.dialogFormVisible) {
              this.toggleSelection();
            }
          });
      // ajax
      //   .get(`${this.$config.gateUrl}/mcpServer/apiList`, {...this.diaform,pageNum:this.pageNum,pageSize:this.pageSize})
      //   .then((r) => {
      //     this.apiList = r.data;
      //     if(this.dialogFormVisible){
      //     this.toggleSelection()

      //     }

      //       });
    },
    toggleSelection() {
      const filterids = this.apiList
          .filter((obj) => this.form.apiIds.includes(obj.apiId))
          .map((obj) => obj);
      console.log(filterids, "filterids");
      if (filterids) {
        this.$nextTick(() => {
          filterids.forEach((row) => {
            console.log(
                row,
                this.$refs.multipleTable.toggleRowSelection,
                "row"
            );
            this.$refs.multipleTable.toggleRowSelection(row, true);
          });
        });
      }
    },
    getseverclass() {
      ajax
          .get(`${this.$config.gateUrl}/mcpServer/getServerClass`, {})
          .then((r) => {
            this.serverClassList = r.data;
          });
    },
    getopenRange() {
      ajax
          .get(`${this.$config.gateUrl}/mcpServer/getOpenRange`, {})
          .then((r) => {
            this.openRangeList = r.data;
          });
    },
    getDetail() {
      ajax
          .get(`${this.$config.gateUrl}/mcpServer/detailMcp`, {
            mcpId: this.seqId,
          })
          .then((r) => {
              r.data.serverJson = r.data.serverJson
                  ? JSON.parse(r.data.serverJson)
                  : {};
              if (r.data.envParams) {
                r.data.envParams.forEach(item => {
                  item.isRequired = item.isRequired == 1 ? true : false;
                })
              }

              this.formMcp = r.data;

          });
    },
    openapi() {
      if (this.form.serverClass) {
        this.dialogFormVisible = true;
        this.diaform.serverClass = this.form.serverClass;
        this.getapilist();
      } else {
        this.$message.error("请先选择分类");
      }
    },
    classChange() {
      this.apiList = [];
      this.newapiIds = "";
      this.form.apiIds = [];
      this.newapiIdsList = [];
    },
    apiidChange() {
      this.newapiIdsList = [];
      this.multipleSelection.forEach((item) => {
        this.newapiIdsList.push(item.apiId);
      });
      this.dialogFormVisible = false;
      // this.idtoname(this.newapiIdsList.join(","));
      this.newapiIds = this.multipleSelection.map((item) => {
        return item.cnApiName;
      });
      this.form.apiIds = this.newapiIdsList;
      console.log(this.newapiIdsList, this.newapiIds, this.form.apiIds);
    },
    idtoname(str) {
      const idArray = str.split(",");
      const _this = this;
      console.log(idArray, "idArray", this.apiList);
      // 使用 filter() 方法筛选出符合 id 数组中的 id 的对象，然后用 map() 方法获取对应的 name 属性
      // let timeinterview = setInterval(function() {
      //       console.log('Hello every 2 seconds!');
      //       _this.idtoname(str);
      //   }, 2000);
      if (_this.apiList.length > 0) {
        // clearInterval(timeinterview);
        const filteredNames = _this.apiList
            .filter((obj) => idArray.includes(obj.apiId))
            .map((obj) => obj.cnApiName);

        // 使用 join() 方法将 name 组合成字符串，以 , 分隔
        const result = filteredNames.join(", ");
        console.log(result, "result");
        // return result;
        this.newapiIds = result;
      } else {
        setTimeout(function () {
          _this.idtoname(str);
        }, 1000);
      }
    },
    changeServerClass() {
      // this.form.serverClass = this.newserverClass.join(",");
    },
    onSuccess() {

      if (this.activeName == "first") {
        const url = this.seqId ? `/mcpServer/updateMcp` : `/mcpServer/saveMcp`;
        this.$refs.form.validate((valid) => {
          if (valid) {
            this.loading = true;
            ajax
                .post(`${this.$config.gateUrl}${url}`, {
                  ...this.form,
                  pageType: this.pageType,
                })
                .then((r) => {
                  if (r.code == 1000) {
                    this.$message.success(`${this.dialogName}成功`);

                    this.$emit("success");
                  } else {
                    this.$message.error(r.data);
                  }
                  this.loading = false;
                });
          }
        });
      } else {
        const url = this.seqId ? `/mcpServer/updateMcp` : `/mcpServer/saveMcp`;
        this.$refs.formMcp.validate((valid) => {
          if (valid) {
            if (Object.keys(this.formMcp.serverJson).length === 0) {
              this.$message.warning(`请输入服务配置`);
              return;
            }
            // this.formMcp.serverJson = JSON.stringify(this.formMcp.serverJson);
            if (this.formMcp.envParams && this.formMcp.envParams.length > 0) {
              this.formMcp.envParams.forEach(item => {
                item.isRequired = item.isRequired ? 1 : 0;
              })
            }

            this.loading = true;
            ajax
                .post(`${this.$config.gateUrl}${url}`, {
                  ...this.formMcp,
                  pageType: this.pageType,
                  serverJson: JSON.stringify(this.formMcp.serverJson)
                })
                .then((r) => {
                  if (r.code == 1000) {
                    this.$message.success(`${this.dialogName}成功`);

                    this.$emit("success");
                  } else {
                    this.$message.error(r.data);
                  }
                  this.loading = false;
                });
          }
        });
      }
    },
    getDialogContainerWidth() {
      if (this.activeName == "first") {
        return "800px";
      } else {
        return "1100px";

      }
    },
    handleMEClick() {
      alert("点击了编辑器");
    }
  },
  mounted() {
    this.$nextTick(() => {
      let ati = document.querySelector(".auto-textarea-input");

      ati.onfocus = () => {
        this.MEPlaceholder = " ";

      };
      ati.onblur = () => {
        if (this.formMcp.readme == null || this.formMcp.readme.length == 0) {
          this.MEPlaceholder = this.MEPlaceholderText;
        }

      };
    });
  },
};
</script>

<style lang="less" scoped>
.apiclass {
  /deep/ .el-select-dropdown__item {
    height: auto;
  }
}

/deep/ .el-checkbox-group {
  height: 300px;
  overflow-y: scroll;
  overflow-x: hidden;
}

/deep/ .el-table-column--selection .cell {
  padding-left: 14px;
  padding-right: 14px;
  padding-top: 14px;
}

.el-select {
  width: 100%;
}

.mcp-box {
  .zhuyi {
    font-size: 14px;
    background: rgb(230, 247, 255);
    line-height: 22px;
    margin-bottom: 40px;
  }

  .mcp-box-item {
    margin-bottom: 10px;
    padding: 0 50px;

    .mcp-title {
      font-weight: 500;
      margin-bottom: 3px;

      span {
        position: relative;
        color: rgb(254, 124, 128);
      }
    }

    .p1 {
      margin-bottom: 10px;
      font-weight: 600;
      font-size: 18px;
    }

    .p2 {
      margin-bottom: 10px;
    }
  }
}

/deep/ .el-checkbox__input {
  top: -10px;
}

.editBox {
  display: flex;
  flex-flow: column wrap;

  /deep/ .el-form-item__content {
    margin-left: 0 !important;
  }

  /deep/ .el-form-item__label {
    text-align: start;
    width: 100% !important;
  }

  i {
    color: #22AAFF;
    font-size: 20px;
  }
}

/deep/ .markdown-body {
  border-left: 1px solid #dcdfe6 !important;
  border-right: 1px solid #dcdfe6 !important;
}

/deep/ .jsoneditor-poweredBy {
  display: none !important;
}

.textLeft {

  /deep/ .el-form-item__label {
    text-align: start;
  }
}

/deep/ .el-dialog {
  transition: width 0.2s linear;
}

/deep/ .envParamsCard {
  transition: width 0.2s linear;
  background-color: #FBFCFE;
  border-color: #DCE1F1;

  .el-card__body {
    padding: 16px;
  }

  .el-form-item__label {
    line-height: 20px;
    font-size: 16px;
    font-weight: 500;
    opacity: 0.8;
    width: 100% !important;
    margin-bottom: 10px;
  }
}


/deep/ .jsoneditor-vue {
  height: 277px;
}

/deep/ .jsoneditor {
  border: none;
  border-radius: 4px;

  .ace_scroller {
    background-color: #FBFCFE !important;
    border-color: #DCE1F1;
  }

  .ace-jsoneditor {
    border: 1px solid #DCE1F1;
  }

}

.MEClass {
  /deep/ textarea {
    height: 1050px;
  }
}
</style>
