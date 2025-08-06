package cn.com.ctsi.mcp.controller;

import cn.com.ctsi.mcp.entity.McpServerInfoDto;
import cn.com.ctsi.mcp.entity.McpServerInfoReq;
import cn.com.ctsi.mcp.service.McpServerInfoService;
import cn.com.ctsi.mcp.service.ServerApiInfoService;
import cn.com.ctsi.mcp.entity.dto.ResultVo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/mcpServer")
@RequiredArgsConstructor
@Slf4j
public class McpServerController {
    private final ServerApiInfoService serverApiInfoService;

    private final McpServerInfoService mcpServerInfoService;

    //"mcp新增-获取分类字典值"
    @GetMapping("/getServerClass")
    public ResultVo getServerClass() {
        log.info("REST request to /mcpServer/getServerClass");
        return mcpServerInfoService.getServerClass();
    }

    //"mcp新增-获取开放范围字典值"
    @GetMapping("/getOpenRange")
    public ResultVo getOpenRange() {
        log.info("REST request to /mcpServer/getOpenRange");
        return mcpServerInfoService.getOpenRange();
    }

    //"mcp查询-分页查询mcp服务信息"
    @PostMapping("/queryMcpServerInfoList")
    public ResultVo queryMcpServerInfoList(@RequestBody McpServerInfoReq req) {
        log.info("REST request to /mcpServer/queryMcpServerInfoList");
        return mcpServerInfoService.queryMcpServerInfoList(req);
    }

    //"mcp新增"
    @PostMapping("/saveMcp")
    public ResultVo saveMcp(@RequestBody McpServerInfoDto dto, HttpServletRequest request){
        log.info("REST request to /mcpServer/saveMcp");
        return mcpServerInfoService.saveMcp(dto,request);
    }

    //"mcp详情"
    @GetMapping("/detailMcp")
    public ResultVo detailMcp(@RequestParam long mcpId){
        log.info("REST request to /mcpServer/detailMcp");
        return mcpServerInfoService.detailMcp(mcpId);
    }

    //"mcp背景随机8张图标"
    @PostMapping("/randomIcon")
    public ResultVo randomIcon(){
        log.info("REST request to /mcpServer/randomIcon");
        return mcpServerInfoService.randomIcon();
    }

}
