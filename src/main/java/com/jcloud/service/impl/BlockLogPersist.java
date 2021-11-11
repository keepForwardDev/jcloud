package com.jcloud.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.jcloud.consts.Const;
import com.jcloud.entity.SysLog;
import com.jcloud.mapper.SysLogMapper;
import com.jcloud.service.LogPersistStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * 直接保存日志
 * @author jiaxm
 * @date 2021/9/18
 */
@Profile(value = {Const.PROFILE_DEV, Const.PROFILE_TEST})
@Service
public class BlockLogPersist implements LogPersistStrategy {

    @Autowired
    private SysLogMapper sysLogMapper;

    @Override
    public Long persist(SysLog sysLog) {
        sysLog.setId(IdWorker.getId());
        sysLogMapper.insert(sysLog);
        return sysLog.getId();
    }
}
