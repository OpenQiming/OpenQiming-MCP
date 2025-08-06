package cn.com.ctsi.mcp.service.impl;

import cn.com.ctsi.mcp.dao.ServerApiInfoDao;
import cn.com.ctsi.mcp.dao.WscMcpApiDao;
import cn.com.ctsi.mcp.entity.ServerApiInfo;
import cn.com.ctsi.mcp.entity.WscMcpApi;
import cn.com.ctsi.mcp.service.ServerApiInfoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServerApiInfoServiceImpl extends ServiceImpl<ServerApiInfoDao, ServerApiInfo> implements ServerApiInfoService {

    private final WscMcpApiDao wscMcpApiDao;
    private final ServerApiInfoDao serverApiInfoDao;

    @Override
    public List<ServerApiInfo> getApiInfoByMcpId(long mcpId) {
        LambdaQueryWrapper<WscMcpApi> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WscMcpApi::getMcpId, mcpId);
        List<WscMcpApi> wscMcpApis = wscMcpApiDao.selectList(queryWrapper);

        List<String> apiIds = wscMcpApis.stream().map(WscMcpApi::getApiId).collect(Collectors.toList());

        if (!apiIds.isEmpty()) {
            LambdaQueryWrapper<ServerApiInfo> apiInfoQueryWrapper = new LambdaQueryWrapper<>();
            apiInfoQueryWrapper.in(ServerApiInfo::getApiId, apiIds);
            return serverApiInfoDao.selectList(apiInfoQueryWrapper);
        }
        return Collections.emptyList();
    }

    /**
     * 根据McpId获取api数量
     * @param mcpId
     * @return api数量
     */
    @Override
    public int getApiNumByMcpId(Long mcpId) {
        // 根据mcpId获取相关的apiId
        LambdaQueryWrapper<WscMcpApi> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WscMcpApi::getMcpId, mcpId);
        List<WscMcpApi> wscMcpApis = wscMcpApiDao.selectList(queryWrapper);
        return wscMcpApis.isEmpty() ? 0 : wscMcpApis.size();
    }

}
