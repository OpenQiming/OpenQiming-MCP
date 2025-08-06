<template>
  <div>
    <div class="mcptotal-box">
      <div class="mcptotal-box-box" v-loading="loading">
        <div class="mcptotal-box-title">
          <img class="imgleft" src="/static/mcp/top.png" alt="" />
          <div class="right-title">
            MCP统计分析面板
            <img src="/static/mcp/刷新.png" alt="" @click="refresh()" />
          </div>
          <div class="right-dec">实时监控MCP服务使用情况</div>
        </div>
        <div class="mcptotal-box-cards">
          <span class="card">
            <img src="/static/mcp/cishu1.png" alt="" />
            <div class="card-title">{{ summary.totalCalls }}</div>
            <div class="card-num">总调用次数</div>
          </span>
          <span class="card">
            <img src="/static/mcp/cishu2.png" alt="" />
            <div class="card-title">{{ summary.managedMcps }}</div>
            <div class="card-num">已纳管MCP数量</div>
          </span>
          <span class="card" v-if="false">
            <img src="/static/mcp/cishu3.png" alt="" />
            <div class="card-title">{{ summary.calledMcps }}</div>
            <div class="card-num">被调用MCP数量</div>
          </span>
        </div>
        <div class="mcptotal-box-charts">
          <div class="mcptotal-box-charts-box">
            <div class="mcptotal-box-charts-box-title">
              <span class="left-span"></span>
              <span class="title">热门MCP排行</span>
            </div>
            <div class="mcptotal-box-charts-box-content1">
              <div class="card-box" v-for="(item, index) in mcpHotRank" :key="index">
                <img :src="getImage(item)" alt="" />
                <div class="title">{{ item.name }}</div>
                <el-tooltip class="item" effect="dark" placement="top">
                  <div slot="content">
                    {{ item.description }}
                  </div>
                  <div class="desc">
                    {{ item.description }}
                  </div>
                </el-tooltip>


                <span class="count">{{ item.totalCalls }}次</span>
              </div>
            </div>
          </div>
          <div class="mcptotal-box-charts-box">
            <div class="mcptotal-box-charts-box-title">
              <span class="left-span"></span>
              <span class="title">MCP省份分布</span>
            </div>
            <div class="mcptotal-box-charts-box-content">
              <div id="pieEcharts"></div>
            </div>
          </div>
        </div>
        <div class="mcptotal-zhu">
          <div class="mcptotal-box-charts-box-title">
            <span class="left-span"></span>
            <span class="title">热门API排行</span>
          </div>
          <div id="zhuEcharts"></div>
        </div>
        <div class="mcptotal-bottom">
          <div class="mcptotal-box-charts-box-title">
            <span class="left-span"></span>
            <span class="title">热门MCP详情</span>
          </div>
          <div class="mcptotal-bottom-box">
            <div class="card" v-for="(item, index) in hotMcpDetail" :key="index">
              <div class="title-box">
                <img :src="getImage(item)" alt="" />
                <span class="title">{{ item.name }}</span>
                <span class="count">{{ item.totalCalls }}次</span>
              </div>
              <div class="content-box" v-for="(item1, index1) in item.tools" :key="index1">
                <span class="title">{{ item1.name }}</span>
                <span class="count">{{ item1.calls }}次</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import echarts from "echarts";
import { name } from "file-loader";
export default {
  components: {},
  data() {
    return {
      summary: {},
      mcpHotRank: [],
      hotMcpDetail: [],
      loading: false,
      chartOption: {
        tooltip: {
          trigger: "item",
          formatter: "{b}:{c}个（{d}%）",
          transitionDuration: 0,
        },
        title: [{
          text: "MCP省份分布占比统计",
          left: "15%",
          bottom: "2%",
        }, {

        }],
        legend: {
          top: "30%",
          right: "10%",
          width: "60%",
          height: "80%",
          orient: "vertical",
        },
        series: [
          {
            name: "Access From",
            type: "pie",
            width: "40%",
            left: "4%",
            top: "10%",

            radius: ["40%", "70%"],
            avoidLabelOverlap: false,
            label: {
              show: true,
              position: "center",
              formatter: "总数121212",
              color: "black",
              fontSize: 20,
            },
            emphasis: {
              label: {
                show: false,
                fontSize: 20,
                fontWeight: "bold",
              },
            },
            labelLine: {
              show: false,
            },
            data: [],
            itemStyle: {
              normal: {
                color: function (colors) {
                  var colorList = [
                    "#56FAFF",
                    "#FF5656",
                    "#FF56AB",
                    "#8EB9FF",
                    "#EA58FA",
                    "#4DE74D",
                    "#24B454",
                    "#FAC858",
                  ];
                  return colorList[colors.dataIndex];
                },
              },
            },
          },
        ],
      },
      chart: null,
      chartOption2: {
        title: {
          text: "",
          left: "45%",
        },
        xAxis: {
          type: "category",
          data: [],
          axisLabel: {
            rotate: 0,
            interval: 0, // 设置 x 轴标签不省略
          },
        },
        legend: {
          top: 40,
          left: "47%",
        },
        yAxis: {
          type: "value",
        },
        grid: {
          left: "3%",
          right: "4%",
          bottom: "2%",
          top: 80,
          containLabel: true,
        },
        series: [
          {
            data: [],
            name: "",
            type: "bar",
            barWidth: 50,
            label: {
              show: true,
              position: "top",
            },
            //  markLine: {
            //       data: [

            //       ],
            //       lineStyle:{
            //         color:'#FF3535'
            //       }
            //   },
            itemStyle: {
              color: new echarts.graphic.LinearGradient(
                0,
                0,
                0,
                1, // 渐变方向从上到下
                [
                  { offset: 0, color: "#1D89F5" }, // 上部颜色
                  { offset: 1, color: "#FFFFFF" }, // 下部颜色
                ]
              ),
            },
          },
        ],
      },
      chart2: null,
    };
  },
  created() {
    this.init();
  },
  methods: {
    init() {
      this.$nextTick(() => {
        this.getPie();
        this.getBar();
      });
      this.getsummary();
      this.getmcpHotRank();
      this.gethotMcpDetail();
    },
    refresh() {
      this.loading = true;
      ajax
        .get(`${this.$config.gateUrl}/mcpServer/statistics/refresh`, {})
        .then((r) => {

          this.init();
        });

    },
    getsummary() {
      ajax
        .post(`${this.$config.gateUrl}/mcpServer/statistics/summary`, {})
        .then((r) => {
          if (r.code == 1000) {
            this.summary = r.data;
          }
        });
    },
    getmcpHotRank() {
      // this.mcpHotRank = [{
      //   name:'aaaaaa',
      //   description:'djuasdg赛道gay大哥idjuasdg赛道gay大哥idjuasdg赛道gay大哥idjuasdg赛道gay大哥idjuasdg赛道gay大哥idjuasdg赛道gay大哥idjuasdg赛道gay大哥idjuasdg赛道gay大哥i'
      // }]
      ajax
        .post(`${this.$config.gateUrl}/mcpServer/statistics/mcpHotRank`, {
          limit: 5,
        })
        .then(async (r) => {
          if (r.code == 1000) {
            
            const mcpdata = structuredClone(r.data);
            for (let i = 0; i < mcpdata.length; i++) {
              const element = mcpdata[i];
              let resp = await ajax
                .get(
                  `${this.$config.gateUrl}/mcpServer/statistics/icon/${element.mcpId}`,
                  {});
                mcpdata[i].url = resp;
            }
            this.mcpHotRank = mcpdata;
            
          }
        });
    },
    gethotMcpDetail() {
      ajax
        .post(`${this.$config.gateUrl}/mcpServer/statistics/hotMcpDetail`, {
          limit: 6,
        })
        .then(async (r) => {
          if (r.code == 1000) {

            const mcpdata = structuredClone(r.data);
            for (let i = 0; i < mcpdata.length; i++) {
              const element = mcpdata[i];
              let resp = await ajax
                .get(
                  `${this.$config.gateUrl}/mcpServer/statistics/icon/${element.mcpId}`,
                  {});
                mcpdata[i].url = resp;
            }
            this.hotMcpDetail = mcpdata;




            console.log(this.hotMcpDetail);

            this.loading = false;
          }
        });
    },
    getPie() {
      ajax
        .post(`${this.$config.gateUrl}/mcpServer/statistics/distribution`, {})
        .then((r) => {
          if (r.code == 1000) {
            const newdata = [];
            r.data.forEach((item) => {
              newdata.push({
                value: item.count,
                name: item.province,
                percentage:item.percentage
              });
            });
            this.chartOption.series[0].data = newdata;
            let count = 0;
            newdata.forEach((item) => {
              count = Number(count) + Number(item.value);
            });
            this.chartOption.series[0].label.formatter = "MCP总数量\n" + count;
            let pieEcharts = document.getElementById("pieEcharts");
            this.chart = echarts.init(pieEcharts);
            const _this = this;
            this.chartOption.legend.formatter = function (name) {
              let series = _this.chartOption.series[0]; // 获取第一个 series 数据
              let data = series.data; // 获取数据项数组
              for (let i = 0; i < data.length; i++) {
                if (data[i].name === name) {
                  // 找到对应的数据项
                  let value = data[i].value; // 获取值
                  let percent =
                    (
                      (value /
                        series.data.reduce(
                          (sum, item) => sum + item.value,
                          0
                        )) *
                      100
                    ).toFixed(2) + "%"; // 计算百分比并格式化
                  return `${name}: ${value}个 (${data[i].percentage}%)`; // 返回格式化后的字符串
                }
              }
              return name; // 如果没有找到，返回名称本身
            };
            this.chart.setOption(this.chartOption, { notMerge: true });
          }
        });
    },
    getBar() {
      ajax
        .post(`${this.$config.gateUrl}/mcpServer/statistics/toolHotRank`, {
          limit: 6,
        })
        .then((r) => {
          if (r.code == 1000) {
            this.mcpHot = r.data;
            const namedata = [];
            const valuedata = [];
            r.data.forEach((item) => {
              namedata.push(item.name);
              valuedata.push(item.calls);
            });
            this.chartOption2.xAxis.data = namedata;
            this.chartOption2.series[0].data = valuedata;
            this.chart2 = echarts.init(document.getElementById("zhuEcharts"));
            this.chart2.setOption(this.chartOption2, { notMerge: true });
          }
        });
    },
    getImage(item) {
      if (item.url != null && item.url != "") {
        return item.url;
      } else {
        return "/static/mcp/默认图标.png";
      }
    },
  },
};
</script>

<style lang="less" scoped>
.mcptotal-box {
  .mcptotal-box-box {
    background-image: url("/static/mcp/背景.png");
    background-repeat: no-repeat;
    background-size: 100% 100%;
    padding: 16px 50px 50px 50px;
  }

  width: 100%;

  height: calc(100vh - 63px);
  overflow-y: auto;

  .mcptotal-box-title {
    background: #ffffff;
    border-radius: 8px 8px 8px 8px;
    height: 122px;

    .imgleft {
      width: 122px;
      height: 122px;
      float: left;
      margin-left: 33px;
      margin-right: 31px;
    }

    .right-title {
      padding-top: 21px;
      margin-bottom: 12px;
      font-weight: 600;
      font-size: 28px;
      color: #303133;

      img {
        width: 22px;
        height: 22px;
        margin-left: 18px;
        cursor: pointer;
      }
    }

    .right-dec {
      font-weight: 500;
      font-size: 16px;
      color: rgb(135, 135, 137);
    }
  }

  .mcptotal-box-cards {
    display: flex;
    justify-content: space-between;
    margin-top: 24px;
    gap: 4%;

    .card {
      // width: 30%;
      flex: 1;
      height: 242px;
      padding: 38px;
      background: #ffffff;
      border-radius: 8px 8px 8px 8px;

      img {
        width: 166px;
        height: 166px;
        margin-right: 40px;
        float: left;
      }

      .card-title {
        font-weight: bold;
        font-size: 43px;
        color: #303133;
        text-align: center;
        padding-top: 31px;
        padding-bottom: 26px;
      }

      .card-num {
        font-weight: 500;
        font-size: 14px;
        color: rgb(135, 135, 137);
        text-align: center;
      }
    }
  }

  .mcptotal-box-charts {
    display: flex;
    justify-content: space-between;
    margin-top: 24px;
    height: 494px;

    .mcptotal-box-charts-box {
      width: 48%;
      background: #ffffff;
      border-radius: 8px;
      padding: 24px 44px;

      .mcptotal-box-charts-box-content {
        >div {
          height: 400px;
        }
      }

      .mcptotal-box-charts-box-content1 {
        padding-top: 33px;

        .card-box {
          position: relative;
          margin-bottom: 31px;

          img {
            width: 40px;
            height: 40px;
            float: left;
            margin-right: 22px;
          }

          .title {
            font-weight: 600;
            font-size: 16px;
            color: #303133;
          }

          .desc {
            font-size: 14px;
            color: rgb(135, 135, 137);
            margin-top: 3px;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
            width: 80%;
          }

          .count {
            position: absolute;
            top: 12px;
            right: 15px;
            font-weight: bold;
            font-size: 14px;
            color: #2b78fe;
          }
        }
      }
    }
  }

  .mcptotal-box-charts-box-title {
    position: relative;

    .left-span {
      height: 18px;
      border-left: 8px solid #2b78fe;
      /* 调整宽度、样式和颜色 */
      position: absolute;
      left: -20px;
      top: 5px;
    }

    .title {
      font-weight: 600;
      font-size: 18px;
      color: #303133;
    }
  }

  .mcptotal-zhu {
    background: #ffffff;
    border-radius: 8px 8px 8px 8px;
    height: 466px;
    margin-top: 24px;
    padding: 24px 44px;

    #zhuEcharts {
      height: 400px;
    }
  }

  .mcptotal-bottom {
    background: #ffffff;
    border-radius: 8px 8px 8px 8px;
    height: 685px;
    margin-top: 24px;
    padding: 24px 44px;

    .mcptotal-bottom-box {
      margin-top: 20px;
      display: flex;
      justify-content: space-between;
      flex-wrap: wrap;

      .card {
        display: inline-block;
        width: 30%;
        height: 260px;
        background: linear-gradient(130deg, #ffffff 0%, #ecf4ff 100%);
        box-shadow: 0px 3px 6px 1px rgba(0, 0, 0, 0.16);
        border-radius: 8px 8px 8px 8px;
        padding: 30px 34px;
        margin-bottom: 40px;

        .title-box {
          position: relative;
          line-height: 31px;
          display: flex;

          img {
            width: 31px;
            height: 31px;
            margin-right: 10px;
          }

          .title {
            font-weight: 600;
            font-size: 16px;
            color: #303133;
          }

          .count {
            position: absolute;
            right: 0;
            top: 0;
            display: inline-block;
            height: 28px;
            background: #2b78fe;
            border-radius: 16px 16px 16px 16px;
            font-size: 16px;
            color: #ffffff;
            padding: 0px 15px;
            line-height: 28px;
          }
        }

        .content-box {
          padding-top: 24px;
          font-size: 14px;
          font-weight: 500;
          color: #303133;
          position: relative;

          .count {
            position: absolute;
            right: 0;
            top: 24px;
          }
        }
      }
    }
  }
}
</style>