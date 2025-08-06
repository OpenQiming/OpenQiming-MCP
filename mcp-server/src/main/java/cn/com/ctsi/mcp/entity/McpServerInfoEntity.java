package cn.com.ctsi.mcp.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * mcp服务信息表
 */
@Data
@TableName(value = "mcp_server_info")
public class McpServerInfoEntity {
    /**
     * 主键id
     */
    @TableId(value = "seq_id")
    private Long seqId;

    /**
     * 服务名称
     */
    @TableField(value = "server_name")
    private String serverName;

    /**
     * 版本号
     */
    @TableField(value = "server_version")
    private String serverVersion;

    /**
     * mcp图片
     */
    @TableField(value = "server_img")
    private String serverImg;

    /**
     * 分类：通信服务、位置服务、数据服务
     */
    @TableField(value = "server_class")
    private String serverClass;

    @TableField(exist = false)
    private List<String> serverClasses;

    /**
     * 状态：待审核、已发布、已拒绝
     */
    @TableField(value = "server_status")
    private String serverStatus;

    /**
     * 服务介绍
     */
    @TableField(value = "server_intro")
    private String serverIntro;

    /**
     * 主要特点
     */
    @TableField(value = "main_feature")
    private String mainFeature;

    /**
     * 使用案例
     */
    @TableField(value = "use_case")
    private String useCase;

    /**
     * 常见问题
     */
    @TableField(value = "common_question")
    private String commonQuestion;

    /**
     * json
     * 服务启动后 写入数据库
     * 服务停止后 清空此字段
     */
    @TableField(value = "server_json")
    private String serverJson;

    /**
     * 创建人id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * newserverid  跳转详情用到的参数
     */
    @TableField(value = "newserverid")
    private Long newserverid;

    @TableField(value = "service_uuid")
    private String serviceUuid;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 区域字段  根据创建人的区域赋值
     */
    @TableField(value = "area")
    private String area;

    /**
     * 开放范围字段
     */
    @TableField(value = "open_range")
    private String openRange;

    /**
     * 接入方式
     * api接入 ： API
     * mcp接入 ： MCP
     */
    @TableField(value = "access_type")
    private String accessType;
    /**
     * README
     */
    @TableField(value = "readme")
    private String readme;


    @TableField(exist = false)
    private List<ServerApiInfo> apiInfos;

    @TableField(exist = false)
    private Integer apiNum;

    @TableField(exist = false)
    private List<ServerApiConfig> envParams;


}
