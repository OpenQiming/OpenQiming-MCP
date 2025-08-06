<script>
import hljs from "highlight.js"
import 'highlight.js/styles/stackoverflow-light.css'
import { js_beautify, css_beautify, html_beautify } from 'js-beautify'
import { mavonEditor } from "mavon-editor";
import "mavon-editor/dist/css/index.css";

// 概览
export default {
  props: ['info'],
  components: {
    mavonEditor,
  },

  data() {
    return {

    }
  },
  methods: {
    copyKey(item) {
      navigator.clipboard.writeText(item)
        .then(() => {
          this.$message.success('已复制到剪贴板！');
        })
        .catch(error => {
          console.error('复制失败:', error);
        });
    }
  },
  mounted() {
    if (this.info.serverJson != null && this.info.serverJson.length > 0) {

      document.querySelector('#jsonPre').innerHTML = js_beautify(
        this.info.serverJson,
        {
          indent_size: 2,
          space_in_empty_paren: true
        }
      )
      hljs.highlightAll();
    }

  }
}
</script>
<template>

  <div class="infoItem__jUvpX">
    <div class="mdContainer__GDLLX">
      <article class="efm_ant-typography css-var-r3">
        <div>
          <div class="lake-content" typography="classic" v-if="info.serverJson != null && info.serverJson.length > 0">



            <h2 id="ClUwj"><span class="ne-text"><span class="blockBlue"></span>接入配置</span></h2>
            <el-button type="text" @click="copyKey(info.serverJson)">复制Key</el-button>
            <pre class="language-json">
                <code id="jsonPre">
                  <!-- {{ info.serverJson }} -->
                </code>
               </pre>

          </div>
          <div class="lake-content" typography="classic" v-else>



            <h2 id="ClUwj"><span class="ne-text"><span class="blockBlue"></span>接入配置</span></h2>
            <div style=""></div>
            <p>暂无数据</p>

          </div>
          <div class="lake-content" typography="classic">
            <h2 id="ClUwj"><span class="ne-text"><span class="blockBlue"></span>README</span></h2>
            <div style="margin-bottom:10px;"></div>
            
            <mavon-editor v-model="info.readme"
                            class="formContent" 
                            :editable="false" 
                            :subfield="false" 
                            defaultOpen="preview" 
                            :toolbarsFlag="false" 
                            v-if="info.readme"
                          ></mavon-editor>
                          <p v-else>暂无数据</p>
          </div>
        </div>
        
      </article>
    </div>
  </div>
</template>
<style lang="less" scoped>
/* ***************************************** */
.tabContent__BV1U2 {
  padding-bottom: 40px;
}

.infoItem__jUvpX {
  margin-bottom: 20px;
}

.infoLabel__d_Q3t {
  color: rgba(38, 36, 76, 0.88);
  margin-bottom: 8px;
  font-size: 13px;
  font-weight: 500;
}

.infoValue__ZV7lb {
  word-break: break-word;
  color: rgba(38, 36, 76, 0.88);
  align-items: center;
  gap: 4px;
  font-size: 14px;
  font-weight: 400;
  line-height: 24px;
  display: flex;
}

.spark-tag-mauve {
  color: var(--efm_ant-color-mauve);
  background: var(--efm_ant-color-mauve-bg);
}

.spark-tag {
  font-weight: 500;
}

.efm_ant-tag {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
  color: rgba(38, 36, 76, 0.88);
  font-size: 12px;

  list-style: none;
  display: inline-block;
  height: auto;
  margin-inline-end: 8px;
  padding-inline: calc(8px - 1px);
  white-space: nowrap;
  background: #fafafb;
  border: 1px solid #cdd0dc;
  border-radius: 4px;
  opacity: 1;
  transition: all 0.2s;
  text-align: start;
  position: relative;
}

.providerTag__K4lR4 {
  border: 1px solid #e6e8ee;
  box-sizing: border-box;
  z-index: 1;
  background-color: #fff;
  border-radius: 6px;
  align-items: center;
  max-width: 100%;
  height: 24px;
  display: flex;
}

.iconWrapper__G5idN {
  background-color: #cdd0dc26;
  border-radius: 6px 0 0 6px;
  align-items: center;
  height: 100%;
  max-height: 23px;
  display: flex;
  overflow: hidden;
}

.iconWrapper__G5idN img {
  -o-object-fit: contain;
  object-fit: contain;
  max-width: 23px;
  max-height: 23px;
  vertical-align: middle;
  border-style: none;
}

.providerText__f3UxR {
  color: rgba(38, 36, 76, 0.88);
  margin: 0 6px;
}

.mdContainer__GDLLX {
  color: rgba(38, 36, 76, 0.88);
}

.css-var-r3.efm_ant-typography {
  --efm_ant-typography-title-margin-top: 1.2em;
  --efm_ant-typography-title-margin-bottom: 0.5em;
}

.efm_ant-typography {
  color: inherit;
  line-height: 1.7;
}

.efm_ant-typography {
  font-family: var(--efm_ant-font-family);
  font-size: var(--efm_ant-font-size);
  box-sizing: border-box;
}

.efm_ant-typography :first-child {
  margin-top: 0 !important;
}

.efm_ant-typography :last-child {
  margin-bottom: 0 !important;
}

.efm_ant-typography h2 {
  font-size: 1.14286em;
}

.efm_ant-typography h6 {
  font-weight: 500;
  line-height: 1.7777;
  color: inherit !important;
  margin-top: .571429em !important;
  margin-bottom: .571429em !important;
}

.efm_ant-typography p {
  margin-top: .571429em !important;
  margin-bottom: .571429em !important;
}

.efm_ant-typography {
  font-family: var(--efm_ant-font-family);
  font-size: 14px;
  box-sizing: border-box;
}

.efm_ant-typography ul {
  list-style-type: circle;
}

.efm_ant-typography ul,
.efm_ant-typography ol {
  margin-inline: 0;
  margin-block: 0 1em;
  padding: 0;
}

ol,
ul,
dl {
  margin-top: 0;
  margin-bottom: 1em;
}
</style>