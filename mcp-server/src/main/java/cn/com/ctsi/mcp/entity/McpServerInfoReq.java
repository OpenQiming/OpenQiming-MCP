package cn.com.ctsi.mcp.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class McpServerInfoReq {
    /**
     * 当前页
     */
    private Integer index;
    /**
     * 页面大小
     */
    private Integer pageSize;

    /**
     * 服务名称
     */
    private String serverName;

    /**
     * 分类：通信服务、位置服务、数据服务
     */
    private String serverClass;

    /**
     * 状态：待审核、已发布、已拒绝
     */
    private String serverStatus;

    /**
     * 创建人id
     */
    private Long userId;
}
