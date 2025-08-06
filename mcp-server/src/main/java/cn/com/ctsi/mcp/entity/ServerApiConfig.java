package cn.com.ctsi.mcp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import lombok.Data;

/**
 * maas接口配置表
 */
@Data
@TableName(value = "server_api_config")
public class ServerApiConfig {
    /**
     * 主键，api接口配置表主键
     */
    @TableId(value = "seq_id")
    private Long seqId;

    /**
     * maas_dcoos_api表主键
     */
    @TableField(value = "api_id")
    private String apiId;

    /**
     * 签约系统
     */
    @TableField(value = "signed_system")
    private String signedSystem;

    /**
     * 参数类型(1-hearder、2-预请求脚本、3-输入参数、4-输出参数、5-请求示例、6-返回示例、7-IP黑白名单)
     */
    @TableField(value = "param_type")
    private Long paramType;

    /**
     * 参数名称
     *
     * MCP接入配置环境变量时的变量名字段
     */
    @TableField(value = "param_name")
    private String paramName;

    /**
     * 参数值
     */
    @TableField(value = "param_value")
    private String paramValue;

    /**
     * 数据类型(Number、String、Boolean、Array、Object)
     *
     * MCP接入配置环境变量时的类型字段
     */
    @TableField(value = "data_type")
    private String dataType;

    /**
     * 数据位置
     */
    @TableField(value = "param_position")
    private String paramPosition;

    /**
     * 必选(1-是，0-不是)
     *
     * MCP接入配置环境变量时默认0
     */
    @TableField(value = "is_required")
    private Long isRequired;

    /**
     * 说明
     *
     * MCP接入配置环境变量时的描述字段
     */
    @TableField(value = "description")
    private String description;

    /**
     * 数据参数父层id
     */
    @TableField(value = "parent_id")
    private Long parentId;

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
     * 示例值
     *
     * MCP接入配置环境变量时的参数值
     */
    @TableField(value = "example_value")
    private String exampleValue;

    /**
     * 补充说明
     * MCP接入配置环境变量时的详细指引字段
     */
    @TableField(value = "supply_notes")
    private String supplyNotes;

    /**
     * 获取方式
     */
    @TableField(value = "access_way")
    private String accessWay;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerApiConfig that = (ServerApiConfig) o;
        return Objects.equals(paramName, that.paramName) && Objects.equals(dataType, that.dataType) && Objects.equals(isRequired, that.isRequired) && Objects.equals(description, that.description) && Objects.equals(supplyNotes, that.supplyNotes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paramName, dataType, isRequired, description, supplyNotes);
    }
}
