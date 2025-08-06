package cn.com.ctsi.mcp.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.List;

@Data
public class McpServerInfoDto {
    /**
     * mcp_Id
     */
    private Long seqId;
    /**
     * 服务名称
     */
    private String serverName;

    /**
     * 版本号
     */

    private String serverVersion;
    /**
     * 图标
     */
    private String serverImg;

    /**
     * 分类：通信服务、位置服务、数据服务
     */

    private String serverClass;

    /**
     * 状态：待审核、已发布、已拒绝
     */
    private String serverStatus;

    /**
     * 服务介绍
     */

    private String serverIntro;

    /**
     * 主要特点
     */

    private String mainFeature;

    /**
     * 使用案例
     */

    private String useCase;

    /**
     * 常见问题
     */

    private String commonQuestion;

    /**
     * json
     */
    private String serverJson;

    private List<String> apiIds;

    private Long userId;

    /**
     * 我的mcp页面 给1
     * mcp管理页面 给2
     * 用于控制不同页面编辑后需要修改成的状态
     */
    private int pageType;

    /**
     * 区域字段  根据创建人的区域赋值
     */
    private String area;

    /**
     * 开放范围字段
     */
    private String openRange;
    /**
     * 接入方式
     * api接入 ： API
     * mcp接入 ： MCP
     */
    private String accessType;
    /**
     * README
     */
    private String readme;

    private List<ServerApiConfig> envParams;


}
