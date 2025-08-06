package cn.com.ctsi.mcp.service;

import cn.com.ctsi.mcp.entity.ServerApiInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ServerApiInfoService extends IService<ServerApiInfo>{

    List<ServerApiInfo> getApiInfoByMcpId(long mcpId);

    int getApiNumByMcpId(Long seqId);
}

