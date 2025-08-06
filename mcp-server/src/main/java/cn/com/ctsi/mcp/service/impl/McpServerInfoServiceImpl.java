package cn.com.ctsi.mcp.service.impl;

import cn.com.ctsi.constant.Constant;
import cn.com.ctsi.dict.entity.AesDictDto;
import cn.com.ctsi.dict.service.AesDictService;
import cn.com.ctsi.mcp.dao.McpServerInfoDao;
import cn.com.ctsi.mcp.dao.WscMcpApiDao;
import cn.com.ctsi.mcp.entity.McpClassSelectBoxRes;
import cn.com.ctsi.mcp.entity.McpServerInfoDto;
import cn.com.ctsi.mcp.entity.McpServerInfoEntity;
import cn.com.ctsi.mcp.entity.McpServerInfoReq;
import cn.com.ctsi.mcp.entity.McpToolTestEntity;
import cn.com.ctsi.mcp.entity.ServerApiConfig;
import cn.com.ctsi.mcp.entity.ServerApiInfo;
import cn.com.ctsi.mcp.entity.WscMcpApi;
import cn.com.ctsi.mcp.entity.dto.ResultVo;
import cn.com.ctsi.mcp.enums.ResultCodeEnum;
import cn.com.ctsi.mcp.service.McpServerInfoService;
import cn.com.ctsi.mcp.service.McpToolTestService;
import cn.com.ctsi.mcp.service.ServerApiConfigService;
import cn.com.ctsi.mcp.service.ServerApiInfoService;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class McpServerInfoServiceImpl extends ServiceImpl<McpServerInfoDao, McpServerInfoEntity> implements McpServerInfoService {

    private final McpServerInfoDao mcpServerInfoDao;
    private final WscMcpApiDao wscMcpApiDao;
    private final ServerApiInfoService serverApiInfoService;
    private final ServerApiConfigService serverApiConfigService;
    private final McpToolTestService mcpToolTestService;
    private final AesDictService aesDictService;

    @Override
    public ResultVo getServerClass() {
        LambdaQueryWrapper<AesDictDto> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AesDictDto::getDictType, Constant.MCP_SERVER_CLASS);
        List<AesDictDto> dictDtoList = aesDictService.list(queryWrapper);
        List<McpClassSelectBoxRes> dictMap = dictDtoList
                .stream()
                .map(this::convertToMcpClassBoxItem)
                .collect(Collectors.toList());
        return new ResultVo(dictMap);
    }

    @Override
    public ResultVo getOpenRange() {
        LambdaQueryWrapper<AesDictDto> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AesDictDto::getDictType, Constant.MCP_SERVER_OPEN_RANGE);
        List<AesDictDto> dictDtoList = aesDictService.list(queryWrapper);
        List<McpClassSelectBoxRes> dictMap = dictDtoList
                .stream()
                .map(this::convertToMcpClassBoxItem)
                .collect(Collectors.toList());
        return new ResultVo(dictMap);
    }

    private McpClassSelectBoxRes convertToMcpClassBoxItem(AesDictDto aesDictDto) {
        McpClassSelectBoxRes mcpClass = new McpClassSelectBoxRes();
        mcpClass.setLabel(aesDictDto.getDictName());
        mcpClass.setValue(aesDictDto.getDictCode());
        return mcpClass;
    }

    @Override
    public ResultVo queryMcpServerInfoList(McpServerInfoReq req) {
        int index = ObjectUtil.isNull(req.getIndex()) ? 1 : req.getIndex();
        int pageSize = ObjectUtil.isNull(req.getPageSize()) ? 10 : req.getPageSize();
        IPage<McpServerInfoEntity> page = new Page<>(index, pageSize);
        LambdaQueryWrapper<McpServerInfoEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.like(StringUtils.isNotBlank(req.getServerName()), McpServerInfoEntity::getServerName, req.getServerName());
        queryWrapper.eq(StringUtils.isNotBlank(req.getServerStatus()) && !Constant.WHOLE_NAME.equals(req.getServerStatus()), McpServerInfoEntity::getServerStatus, req.getServerStatus());
        queryWrapper.eq(ObjectUtil.isNotNull(req.getUserId()), McpServerInfoEntity::getUserId, req.getUserId());
        if (StringUtils.isNotBlank(req.getServerClass()) && !Constant.WHOLE_NAME.equals(req.getServerClass())) {
            /*List<String> serverClassList = Arrays.asList(req.getServerClass().split(","));
            // 使用循环构建多个 server_class like 条件
            queryWrapper.and(wrapper -> {
                boolean isFirst = true;
                for (String serverClass : serverClassList) {
                    if (isFirst) {
                        wrapper.like(McpServerInfoEntity::getServerClass, serverClass);
                        isFirst = false;
                    } else {
                        wrapper.or().like(McpServerInfoEntity::getServerClass, serverClass);
                    }
                }
            });*/
            queryWrapper.like(McpServerInfoEntity::getServerClass, req.getServerClass());
        }
        queryWrapper.orderByDesc(McpServerInfoEntity::getCreateTime);
        page = this.page(page, queryWrapper);
        List<McpServerInfoEntity> records = page.getRecords();
        if (CollectionUtils.isNotEmpty(records)) {
            for (McpServerInfoEntity record : records) {
                // 获取api数量
                int apiNum = serverApiInfoService.getApiNumByMcpId(record.getSeqId());
                record.setApiNum(apiNum);
                List<String> serverClasses = new ArrayList<>();
                if (StringUtils.isNotEmpty(record.getServerClass())) {
                    String[] strings = record.getServerClass().split(",");
                    for (String string : strings) {
                        LambdaQueryWrapper<AesDictDto> queryWrapper2 = new LambdaQueryWrapper<>();
                        queryWrapper2.eq(AesDictDto::getDictType, Constant.MCP_SERVER_CLASS);
                        queryWrapper2.eq(AesDictDto::getDictCode, string);
                        AesDictDto dictDto = aesDictService.getOne(queryWrapper2);
                        serverClasses.add(dictDto.getDictName());
                    }
                }
                record.setServerClasses(serverClasses);
            }
        }
        return new ResultVo(page);
    }



    @Transactional
    @Override
    public ResultVo saveMcp(McpServerInfoDto dto, HttpServletRequest request) {
        // mcp server name 重名拦截
        LambdaQueryWrapper<McpServerInfoEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(McpServerInfoEntity::getServerName, dto.getServerName());
        List<McpServerInfoEntity> mcpServerInfoEntities = mcpServerInfoDao.selectList(queryWrapper);
        if (!mcpServerInfoEntities.isEmpty()) {
            return new ResultVo(ResultCodeEnum.FAILED, "mcp server name重复!");
        }
        boolean isApiAccess = isApiAccess(dto);
        saveMcpServerInfo(dto, request, isApiAccess);

        // api接入
        if (Objects.nonNull(dto.getApiIds()) && !dto.getApiIds().isEmpty()) {
            saveMcpApi(dto);
        }
        // mcp接入
        if (Objects.nonNull(dto.getEnvParams()) && !dto.getEnvParams().isEmpty()) {
            saveMcpEnv(dto);
        }

        if (isApiAccess) {
            // api接入
            if (dto.getServerClass().equals("01")) {
                // 如果是云网精选 需要调用其他接口
                try {
                    return new ResultVo(ResultCodeEnum.SUCCESS);
                } catch (Exception e) {
                    String message = e.getMessage();
                    // 手动回滚
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return new ResultVo(ResultCodeEnum.FAILED, message);
                }

            } else {
                // 如果不是云网精选 不再调用其他接口
                return new ResultVo(ResultCodeEnum.SUCCESS);
            }
        } else {
            // mcp接入
            return new ResultVo(ResultCodeEnum.SUCCESS);
        }
    }

    /**
     * 是否时api接入
     */
    private boolean isApiAccess(McpServerInfoDto dto) {
        String accessType = Optional.ofNullable(dto.getAccessType()).orElse(Constant.MCP_SERVER_ACCESS_TYPE_API);
        dto.setAccessType(accessType);
        return Constant.MCP_SERVER_ACCESS_TYPE_API.equals(accessType);
    }

    private void saveMcpServerInfo(McpServerInfoDto dto, HttpServletRequest request, boolean isApiAccess) {
        // 获取创建人Id
        //Long createId = tokenProviderService.getUserId(request);
        Long createId = IdUtil.getSnowflakeNextId();
        long mcpId = IdUtil.getSnowflakeNextId();
        dto.setSeqId(mcpId);

        McpServerInfoEntity mcp = new McpServerInfoEntity();
        mcp.setSeqId(mcpId);
        mcp.setServerName(dto.getServerName());
        mcp.setServerVersion(dto.getServerVersion());
        if (StrUtil.isNotBlank(dto.getServerImg())) {
            mcp.setServerImg(dto.getServerImg());
        }
        mcp.setServerClass(dto.getServerClass());
        mcp.setServerIntro(dto.getServerIntro());
        mcp.setMainFeature(dto.getMainFeature());
        mcp.setUseCase(dto.getUseCase());
        mcp.setCommonQuestion(dto.getCommonQuestion());
        mcp.setServerJson(dto.getServerJson());
        mcp.setUserId(createId);
        // 区域
        mcp.setArea(dto.getArea());
        // 开放范围
        mcp.setOpenRange(dto.getOpenRange());
        // readme
        mcp.setReadme(dto.getReadme());
        // 接入类型
        mcp.setAccessType(dto.getAccessType());

        if (isApiAccess) {
            // api接入
            if (dto.getServerClass().equals("03") || dto.getServerClass().equals("02")) {
                // 如果是开源热门,生态合作 状态设置为已发布
                mcp.setServerStatus(Constant.MCP_SERVER_STATUS_PUBLISHED);
            } else {
                // 如果是云网精选 状态设置为已创建
                mcp.setServerStatus(Constant.MCP_SERVER_STATUS_CREATE);
            }
        } else {
            // mcp接入 状态直接设置为已创建
            mcp.setServerStatus(Constant.MCP_SERVER_STATUS_PUBLISHED);
        }

        mcp.setCreateTime(new Date());
        mcpServerInfoDao.insert(mcp);
    }

    /**
     * MCP接入时 保存环境变量
     */
    private void saveMcpEnv(McpServerInfoDto dto) {
        Long mcpId = dto.getSeqId();
        String apiId = IdUtil.getSnowflakeNextIdStr();
        // server_api_info表->  添加一下虚拟api信息 状态设置为MCP
        ServerApiInfo serverApiInfo = new ServerApiInfo();
        serverApiInfo.setApiId(apiId);
        serverApiInfo.setApiName(dto.getServerName());
        serverApiInfo.setApiState("MCP");
        serverApiInfoService.save(serverApiInfo);
        // wsc_mcp_api表-> 保存虚拟api与mcp的关联关系
        WscMcpApi wscMcpApi = new WscMcpApi();
        wscMcpApi.setSeqId(IdUtil.getSnowflakeNextId());
        wscMcpApi.setMcpId(mcpId);
        wscMcpApi.setApiId(apiId);
        wscMcpApi.setCreateTime(new Date());
        wscMcpApiDao.insert(wscMcpApi);
        // server_api_config表-> 保存 环境变量
        List<ServerApiConfig> envParams = dto.getEnvParams();
        envParams.forEach(env -> {
            env.setSeqId(IdUtil.getSnowflakeNextId());
            env.setApiId(apiId);
        });
        serverApiConfigService.saveBatch(envParams);
    }

    /**
     * API接入时 保存MCP与API的关联关系
     */
    private void saveMcpApi(McpServerInfoDto dto) {
        List<String> apiIds = dto.getApiIds();
        Long mcpId = dto.getSeqId();
        for (String apiId : apiIds) {
            WscMcpApi wscMcpApi = new WscMcpApi();
            wscMcpApi.setSeqId(IdUtil.getSnowflakeNextId());
            wscMcpApi.setMcpId(mcpId);
            wscMcpApi.setApiId(apiId);
            wscMcpApi.setCreateTime(new Date());
            wscMcpApiDao.insert(wscMcpApi);
        }
    }


    private void deleteServerInfo(long mcpId) {
        LambdaQueryWrapper<McpServerInfoEntity> deleteQueryWrapper = new LambdaQueryWrapper<>();
        deleteQueryWrapper.eq(McpServerInfoEntity::getSeqId, mcpId);
        mcpServerInfoDao.delete(deleteQueryWrapper);
    }

    private void deleteMcpApi(long mcpId) {
        LambdaQueryWrapper<WscMcpApi> deleteQueryWrapper = new LambdaQueryWrapper<>();
        deleteQueryWrapper.eq(WscMcpApi::getMcpId, mcpId);
        wscMcpApiDao.delete(deleteQueryWrapper);
    }

    private void deleteMcpEnv(Long mcpId) {
        // 1.删除旧的环境变量(server_api_config)
        // 2.删除旧的虚拟api(server_api_info)
        // 3.删除旧的MCP与api的关联(wsc_mcp_api)

        LambdaQueryWrapper<WscMcpApi> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(WscMcpApi::getApiId);
        queryWrapper.eq(WscMcpApi::getMcpId, mcpId);
        WscMcpApi wscMcpApis = wscMcpApiDao.selectOne(queryWrapper);
        if (Objects.nonNull(wscMcpApis)) {
            LambdaQueryWrapper<ServerApiConfig> envDeleteWrapper = new LambdaQueryWrapper<>();
            envDeleteWrapper.eq(ServerApiConfig::getApiId, wscMcpApis.getApiId());
            serverApiConfigService.remove(envDeleteWrapper);

            LambdaQueryWrapper<ServerApiInfo> virtualApiDeleteWrapper = new LambdaQueryWrapper<>();
            virtualApiDeleteWrapper.eq(ServerApiInfo::getApiId, wscMcpApis.getApiId());
            serverApiInfoService.remove(virtualApiDeleteWrapper);

            LambdaQueryWrapper<WscMcpApi> relationDeleteWrapper = new LambdaQueryWrapper<>();
            relationDeleteWrapper.eq(WscMcpApi::getApiId, wscMcpApis.getApiId());
            wscMcpApiDao.delete(relationDeleteWrapper);
        }
    }

    @Override
    public ResultVo detailMcp(long mcpId) {
        McpServerInfoEntity mcpServerInfoEntity = mcpServerInfoDao.selectById(mcpId);
        String accessType = Optional.ofNullable(mcpServerInfoEntity.getAccessType()).orElse(Constant.MCP_SERVER_ACCESS_TYPE_API);
        String serverClass = mcpServerInfoEntity.getServerClass();
        if (Constant.MCP_SERVER_ACCESS_TYPE_API.equals(accessType)) {
            if (serverClass.equals("01")) {
                // 获取api信息
                List<ServerApiInfo> apiInfos = null;
                mcpServerInfoEntity.setApiInfos(apiInfos);
            } else {
                // 获取api信息
                List<ServerApiInfo> apiInfos = serverApiInfoService.getApiInfoByMcpId(mcpId);
                for (ServerApiInfo apiInfo : apiInfos) {
                    LambdaQueryWrapper<ServerApiConfig> wrapper = new LambdaQueryWrapper<>();
                    wrapper.eq(ServerApiConfig::getApiId, apiInfo.getApiId());
                    List<ServerApiConfig> list = serverApiConfigService.list(wrapper);
                    apiInfo.setServerApiConfigList(list);
                }
                mcpServerInfoEntity.setApiInfos(apiInfos);
            }
        } else {
            // 获取env信息
            LambdaQueryWrapper<WscMcpApi> relationWrapper = new LambdaQueryWrapper<>();
            relationWrapper.select(WscMcpApi::getApiId);
            relationWrapper.eq(WscMcpApi::getMcpId, mcpId);
            WscMcpApi relation = wscMcpApiDao.selectOne(relationWrapper);
            // 处理没有环境变量的情况
            if (Objects.nonNull(relation)) {
                ServerApiInfo virtualApi = serverApiInfoService.getById(relation.getApiId());

                LambdaQueryWrapper<ServerApiConfig> envWrapper = new LambdaQueryWrapper<>();
                envWrapper.eq(ServerApiConfig::getApiId, virtualApi.getApiId());
                List<ServerApiConfig> envs = serverApiConfigService.list(envWrapper);
                mcpServerInfoEntity.setEnvParams(envs);
            } else {
                mcpServerInfoEntity.setEnvParams(Collections.emptyList());
            }
        }
        return new ResultVo(mcpServerInfoEntity);
    }


    private void dealMcpToolTestData(McpServerInfoDto dto) {
        //清除mcp服务的测试记录
        Long mcpId = dto.getSeqId();
        LambdaQueryWrapper<McpToolTestEntity> deleteQueryWrapper = new LambdaQueryWrapper<>();
        deleteQueryWrapper.eq(McpToolTestEntity::getMcpId, mcpId);
        mcpToolTestService.remove(deleteQueryWrapper);
    }

    private void dealMcpApi(McpServerInfoDto dto) {
        Long mcpId = dto.getSeqId();
        // 先删除 旧数据
        deleteMcpApi(mcpId);
        saveMcpApi(dto);
    }

    private void dealMcpEnv(McpServerInfoDto dto) {
        // 先删除 旧数据
        Long mcpId = dto.getSeqId();
        deleteMcpEnv(mcpId);
        saveMcpEnv(dto);
    }

    private void updateMcpServerInfo(McpServerInfoDto dto, boolean apiAccess) {
        McpServerInfoEntity mcp = new McpServerInfoEntity();
        mcp.setSeqId(dto.getSeqId());
        mcp.setServerName(dto.getServerName());
        mcp.setServerVersion(dto.getServerVersion());
        mcp.setServerImg(dto.getServerImg());
        mcp.setServerClass(dto.getServerClass());
        mcp.setServerIntro(dto.getServerIntro());
        mcp.setMainFeature(dto.getMainFeature());
        mcp.setUseCase(dto.getUseCase());
        mcp.setCommonQuestion(dto.getCommonQuestion());
        mcp.setOpenRange(dto.getOpenRange());
        mcp.setAccessType(dto.getAccessType());
        if (!apiAccess) {
            mcp.setServerJson(dto.getServerJson());
            mcp.setReadme(dto.getReadme());
        }

        if (apiAccess) {
            if (dto.getServerClass().equals("03") || dto.getServerClass().equals("02")) {
                // 如果是开源热门 状态设置为已发布
                mcp.setServerStatus(Constant.MCP_SERVER_STATUS_PUBLISHED);
            } else {
                // 如果不是开源热门
                // 我的mcp页面修改时状态设置为已创建；mcp管理页面修改状态时设置为待审核
                mcp.setServerStatus(dto.getPageType() == 1 ? Constant.MCP_SERVER_STATUS_CREATE : Constant.MCP_SERVER_STATUS_TO_AUDIT);
            }
        } else {
            mcp.setServerStatus(Constant.MCP_SERVER_STATUS_CREATE);
        }
        mcpServerInfoDao.updateById(mcp);
    }

    /**
     * 普通字段是否有修改
     * 有字段变动 返回 true
     */
    private boolean isFieldModified(McpServerInfoEntity oldMcpServerInfoEntity, McpServerInfoDto dto,
                                    boolean apiAccess) {
        boolean isModified = false;
        if (!oldMcpServerInfoEntity.getServerName().equals(dto.getServerName())) {
            isModified = true;
            return isModified;
        }
        if (!oldMcpServerInfoEntity.getServerVersion().equals(dto.getServerVersion())) {
            isModified = true;
            return isModified;
        }
        if (!oldMcpServerInfoEntity.getServerClass().equals(dto.getServerClass())) {
            isModified = true;
            return isModified;
        }
        if (!oldMcpServerInfoEntity.getServerIntro().equals(dto.getServerIntro())) {
            isModified = true;
            return isModified;
        }
        if (!oldMcpServerInfoEntity.getMainFeature().equals(dto.getMainFeature())) {
            isModified = true;
            return isModified;
        }
        if (!oldMcpServerInfoEntity.getUseCase().equals(dto.getUseCase())) {
            isModified = true;
            return isModified;
        }
        if (!oldMcpServerInfoEntity.getCommonQuestion().equals(dto.getCommonQuestion())) {
            isModified = true;
            return isModified;
        }
        // 开放范围
        if (!oldMcpServerInfoEntity.getOpenRange().equals(dto.getOpenRange())) {
            isModified = true;
            return isModified;
        }
        // MCP接入时校验
        if (!apiAccess) {
            // 服务配置
            if (!oldMcpServerInfoEntity.getServerJson().equals(dto.getServerJson())) {
                isModified = true;
                return isModified;
            }
            // readme
            if (!oldMcpServerInfoEntity.getReadme().equals(dto.getReadme())) {
                isModified = true;
                return isModified;
            }
        }
        return isModified;
    }

    /**
     * 校验api是否有修改
     * <p>
     * api有变动 返回 true
     */
    private boolean isApiChanged(McpServerInfoDto dto) {
        boolean isApiChanged = false;

        // 获取原始apiId
        LambdaQueryWrapper<WscMcpApi> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WscMcpApi::getMcpId, dto.getSeqId());
        List<WscMcpApi> oldWscMcpApis = wscMcpApiDao.selectList(wrapper);
        List<String> oldApiIds = oldWscMcpApis.stream().map(WscMcpApi::getApiId).collect(Collectors.toList());
        List<String> apiIds = Optional.ofNullable(dto.getApiIds()).orElse(Collections.emptyList());
        // api接入 校验api
        if (oldApiIds.size() != apiIds.size()) {
            isApiChanged = true;
            return isApiChanged;
        } else {
            for (String oldApiId : oldApiIds) {
                if (!apiIds.contains(oldApiId)) {
                    isApiChanged = true;
                    return isApiChanged;
                }
            }
        }
        return isApiChanged;
    }


    /**
     * 校验环境变量是否有修改
     * <p>
     * 环境变量有变动 返回 true
     */
    private boolean isEnvChanged(McpServerInfoDto dto) {
        boolean isEnvChanged = false;
        List<ServerApiConfig> envs = Optional.ofNullable(dto.getEnvParams()).orElse(Collections.emptyList());
        // 获取原始apiId
        LambdaQueryWrapper<WscMcpApi> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WscMcpApi::getMcpId, dto.getSeqId());
        List<WscMcpApi> oldWscMcpApis = wscMcpApiDao.selectList(wrapper);
        List<String> oldApiIds = oldWscMcpApis.stream().map(WscMcpApi::getApiId).collect(Collectors.toList());
        if (oldApiIds.isEmpty()) {
            //创建时没有设置环境变量
            if (!envs.isEmpty()){
                isEnvChanged = true;
                return isEnvChanged;
            }
        } else {
            // mcp接入 校验环境变量
            String virtualApiId = oldApiIds.get(0);
            LambdaQueryWrapper<ServerApiConfig> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ServerApiConfig::getApiId, virtualApiId);
            List<ServerApiConfig> oldEnvs = serverApiConfigService.list(queryWrapper);
            if (oldEnvs.size() != envs.size()) {
                isEnvChanged = true;
                return isEnvChanged;
            } else {
                // 检查是否有新的环境变量（新环境变量没有seqId,apiId）
                boolean existsNewEnv = envs.stream().anyMatch(env -> StrUtil.isBlank(env.getApiId()));
                if (existsNewEnv) {
                    isEnvChanged = true;
                    return isEnvChanged;
                } else {
                    Map<Long, List<ServerApiConfig>> oldEnvMap = oldEnvs.stream().collect(Collectors.groupingBy(ServerApiConfig::getSeqId));
                    for (ServerApiConfig env : envs) {
                        ServerApiConfig oldEnv = oldEnvMap.get(env.getSeqId()).get(0);
                        if (!oldEnv.equals(env)) {
                            isEnvChanged = true;
                            return isEnvChanged;
                        }
                    }
                }
            }
        }
        return isEnvChanged;
    }


    @Override
    public ResultVo randomIcon() {
        LambdaQueryWrapper<McpServerInfoEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(McpServerInfoEntity::getServerStatus, Constant.MCP_SERVER_STATUS_PUBLISHED);
        queryWrapper.eq(McpServerInfoEntity::getServerClass, "01");
        queryWrapper.isNotNull(McpServerInfoEntity::getServerImg);
        queryWrapper.ne(McpServerInfoEntity::getServerImg, Constant.BLANK_VALUE);
        queryWrapper.orderByDesc(McpServerInfoEntity::getSeqId);
        queryWrapper.last("limit 8");
        List<String> icons = this.list(queryWrapper)
                .stream()
                .map(McpServerInfoEntity::getServerImg)
                .filter(StrUtil::isNotBlank)
                .collect(Collectors.toList());
        if (!icons.isEmpty()) {
            int diff = 8 - icons.size();
            for (int i = 1; i <= diff; i++) {
                icons.add(icons.get(icons.size() - 1));
            }
        }
        return new ResultVo(icons);
    }

    private void buildToolTestData(long mcpId, List<ServerApiInfo> serverApiInfos) {
        // 工具测试记录数据入库  什么时机入库（数据库中没有就入库）
        LambdaQueryWrapper<McpToolTestEntity> existsQueryWrapper = new LambdaQueryWrapper<>();
        existsQueryWrapper.eq(McpToolTestEntity::getMcpId, mcpId);
        List<McpToolTestEntity> mcpToolTestEntities = mcpToolTestService.list(existsQueryWrapper);

        if (mcpToolTestEntities.isEmpty()) {
            List<McpToolTestEntity> toolTestEntities = new ArrayList<>();
            for (ServerApiInfo serverApiInfo : serverApiInfos) {
                McpToolTestEntity mcpToolTestEntity = new McpToolTestEntity();
                mcpToolTestEntity.setSeqId(IdUtil.getSnowflakeNextId());
                mcpToolTestEntity.setMcpId(mcpId);
                // 亿讯 响应中不包含apiId（知识库）
                // mcpToolTestEntity.setApiId(serverApiInfo.getApiId());
                mcpToolTestEntity.setApiName(serverApiInfo.getApiName());
                mcpToolTestEntity.setTestTime(new Date());
                mcpToolTestEntity.setCreateTime(new Date());
                mcpToolTestEntity.setTestStatus(Constant.MCP_TOOL_TEST_STATUES_TO_TEST);
                // 设置为待测试
                serverApiInfo.setTestStatus(Constant.MCP_TOOL_TEST_STATUES_TO_TEST);
                toolTestEntities.add(mcpToolTestEntity);
            }
            mcpToolTestService.saveBatch(toolTestEntities);
        } else {
            Map<String, String> toolTestStatusMap = mcpToolTestEntities.stream().collect(Collectors.toMap(McpToolTestEntity::getApiName, McpToolTestEntity::getTestStatus));
            for (ServerApiInfo serverApiInfo : serverApiInfos) {
                String toolTestStatus = toolTestStatusMap.get(serverApiInfo.getApiName());
                serverApiInfo.setTestStatus(toolTestStatus);
            }
        }
    }

}
