package cn.com.ctsi.mcp.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.com.ctsi.mcp.dao.ServerApiConfigDao;
import cn.com.ctsi.mcp.entity.ServerApiConfig;
import cn.com.ctsi.mcp.service.ServerApiConfigService;
@Service
public class ServerApiConfigServiceImpl extends ServiceImpl<ServerApiConfigDao, ServerApiConfig> implements ServerApiConfigService{

}
