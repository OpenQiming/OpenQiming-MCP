package cn.com.ctsi.dict.service.impl;

import cn.com.ctsi.dict.dao.AesDictDao;
import cn.com.ctsi.dict.entity.AesDictDto;
import cn.com.ctsi.dict.service.AesDictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AesDictServiceImpl extends ServiceImpl<AesDictDao, AesDictDto> implements AesDictService {
}
