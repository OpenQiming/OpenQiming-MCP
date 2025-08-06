package cn.com.ctsi.mcp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * maas能力dcoos接口API
 */
@Data
@TableName(value = "server_api_info")
public class ServerApiInfo {
    /**
     * ApiId，展示在DCOOS注册的接口ApiId
     */
    @TableId(value = "api_id")
    private String apiId;

    /**
     * 接口名称，展示在DCOOS注册的接口名称
     */
    @TableField(value = "api_name")
    private String apiName;

    /**
     * 接口类型（POST、GET、PUT、PATCH、DELETE）
     */
    @TableField(value = "api_type")
    private String apiType;

    /**
     * EOP协议
     */
    @TableField(value = "eop_protocol")
    private String eopProtocol;

    /**
     * 服务协议（http、https、ws、ftp、sftp、ftps）
     */
    @TableField(value = "service_protocol")
    private String serviceProtocol;

    /**
     * EOP调用地址
     */
    @TableField(value = "eop_access_address")
    private String eopAccessAddress;

    /**
     * 接口说明
     */
    @TableField(value = "api_describe")
    private String apiDescribe;

    /**
     * 认证策略
     */
    @TableField(value = "auth_policy")
    private String authPolicy;

    /**
     * 超时时长（非零正整数，小于等于300，单位s）
     */
    @TableField(value = "timeout_duration")
    private Long timeoutDuration;

    /**
     * 开放范围
     */
    @TableField(value = "open_range")
    private String openRange;

    /**
     * 是否公网(0-否，1-是)
     */
    @TableField(value = "is_public_network")
    private String isPublicNetwork;

    /**
     * 所属系统
     */
    @TableField(value = "\"system\"")
    private String system;

    /**
     * 所属区域
     */
    @TableField(value = "area")
    private String area;

    /**
     * 所属区域编码
     */
    @TableField(value = "area_code")
    private String areaCode;

    /**
     * 应用场景大类
     */
    @TableField(value = "large_scene")
    private String largeScene;

    /**
     * 应用场景子类
     */
    @TableField(value = "small_scene")
    private String smallScene;

    /**
     * 接口状态（0SA有效，0SF无效）
     * MCP接入 设置为MCP
     */
    @TableField(value = "api_state")
    private String apiState;

    /**
     * 创建人id
     */
    @TableField(value = "creator_id")
    private Long creatorId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 接口名称（中文）
     */
    @TableField(value = "cn_api_name")
    private String cnApiName;

    @TableField(exist = false)
    private List<ServerApiConfig> serverApiConfigList;

    /**
     * 知识库API查询列表接口 返回的主键id
     */
    @TableField(exist = false)
    private Long id;

    /**
     * 知识库API查询列表接口 返回的适用系统(如何多个 ,分隔)
     */
    @TableField(exist = false)
    private String applySystem;

    /**
     * 工具测试状态
     * 待测试、测试中、成功、失败
     */
    @TableField(exist = false)
    private String testStatus;

}
