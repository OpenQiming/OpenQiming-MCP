package cn.com.ctsi.mcp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * mcp与api的关联表
 */
@Data
@TableName(value = "wsc_mcp_api")
public class WscMcpApi {
    @TableId(value = "seq_id")
    private Long seqId;

    /**
     * 关联mcp主键id
     */
    @TableField(value = "mcp_id")
    private Long mcpId;

    /**
     * 关联api主键id
     */
    @TableField(value = "api_id")
    private String apiId;

    @TableField(value = "create_time")
    private Date createTime;
}
