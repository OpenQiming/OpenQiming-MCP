package cn.com.ctsi.mcp.service;

import cn.com.ctsi.mcp.entity.McpServerInfoDto;
import cn.com.ctsi.mcp.entity.McpServerInfoEntity;
import cn.com.ctsi.mcp.entity.McpServerInfoReq;
import cn.com.ctsi.mcp.entity.dto.ResultVo;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * @author z1099
 */
public interface McpServerInfoService extends IService<McpServerInfoEntity> {

    ResultVo getServerClass();
    ResultVo getOpenRange();

    ResultVo queryMcpServerInfoList(McpServerInfoReq req);

    ResultVo saveMcp(McpServerInfoDto dto, HttpServletRequest request);

    ResultVo detailMcp(long mcpId);

    ResultVo randomIcon();

}
