package cn.com.ctsi.mcp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * mcp工具测试记录表
 */
@Data
@TableName(value = "mcp_tool_test")
public class McpToolTestEntity {
    /**
     * 主键
     */
    @TableId(value = "seq_id")
    private Long seqId;

    /**
     * mcp_id
     */
    @TableField(value = "mcp_id")
    private Long mcpId;

    /**
     * apiId;工具id
     */
    @TableField(value = "api_id")
    private String apiId;

    /**
     * 工具名
     */
    @TableField(value = "api_name")
    private String apiName;

    /**
     * 测试状态（待测试，测试中，成功，失败）
     */
    @TableField(value = "test_status")
    private String testStatus;

    /**
     * 测试时间
     */
    @TableField(value = "test_time")
    private Date testTime;

    /**
     * 执行时间；响应时间
     */
    @TableField(value = "execution_time")
    private Double executionTime;

    /**
     * 状态码
     */
    @TableField(value = "status_code")
    private Integer  statusCode;

    /**
     * 错误消息
     */
    @TableField(value = "error_msg")
    private String errorMsg;

    /**
     * 请求内容
     */
    @TableField(value = "req_content")
    private String reqContent;

    /**
     * 响应内容
     */
    @TableField(value = "res_content")
    private String resContent;

    /**
     * 测试记录创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;
}
