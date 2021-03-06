package com.jcloud.service.impl;

import cn.hutool.extra.servlet.ServletUtil;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.jcloud.bean.ApiRequest;
import com.jcloud.bean.ApiResult;
import com.jcloud.bean.IndexStat;
import com.jcloud.bean.ShiroUser;
import com.jcloud.consts.Const;
import com.jcloud.core.domain.ResponseData;
import com.jcloud.core.service.DefaultOrmService;
import com.jcloud.entity.SysLog;
import com.jcloud.mapper.ClientDetailsMapper;
import com.jcloud.mapper.DepartmentMapper;
import com.jcloud.mapper.SysLogMapper;
import com.jcloud.mapper.UserMapper;
import com.jcloud.service.LogPersistStrategy;
import com.jcloud.service.SyslogService;
import com.jcloud.utils.SecurityUtil;
import com.jcloud.utils.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;

/**
 * @author jiaxm
 * @date 2021/9/16
 */
@Slf4j
@Service
public class SyslogServiceServiceImpl extends DefaultOrmService<SysLogMapper, SysLog, SysLog> implements SyslogService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private LogPersistStrategy logPersistStrategy;

    @Autowired
    private ApiPrivilegeChecker apiPrivilegeChecker;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ClientDetailsMapper clientDetailsMapper;



    @Override
    public SysLog convert(SysLog sysLog) {
        return sysLog;
    }

    @Override
    public ResponseData saveApiLog(SysLog log) {
        ResponseData responseData = ResponseData.getSuccessInstance();
        String apiKey = log.getContent();
        ValueOperations valueOperations = stringRedisTemplate.opsForValue();
        String apiLimit = stringRedisTemplate.opsForValue().get(apiKey);
        if (StringUtils.isNotBlank(apiLimit)) {
            Integer apiLimitValue = Integer.valueOf(apiLimit);
            if (apiLimitValue.longValue() <= 0) { // ????????????
                responseData.setCode(Const.CODE_ERROR);
                responseData.setMsg("??????????????????????????????");
            }
            if (apiLimitValue.intValue() == Integer.MAX_VALUE) { // ????????????
                pass(responseData, log);
            } else { // ????????????
                Long dec = valueOperations.decrement(apiKey);
                if (dec >= 0) {
                    pass(responseData, log);
                } else { // ?????????????????????????????????????????????
                    stringRedisTemplate.opsForValue().set(apiKey, "0", Duration.ofHours(6));
                }
            }
        } else { // ????????????, ????????????
            // apiLimit:2:doctor-data-center:quchuang-admin:/user/checkPhone/{phone}
            ApiRequest apiRequest = new ApiRequest();
            String[] contentArray = log.getContent().split(StringPool.COLON);
            apiRequest.setAppKey(contentArray[2]);
            apiRequest.setApiPath(contentArray[4]);
            apiRequest.setServiceId(contentArray[3]);
            boolean success = apiPrivilegeChecker.process(apiRequest, new ApiResult());
            if (success) {
                saveApiLog(log);
            }
        }
        return responseData;
    }

    @Override
    public ResponseData index() {
        IndexStat indexStat = new IndexStat();
        indexStat.setClientNumber(clientDetailsMapper.selectCount(null));
        indexStat.setUserNumber(userMapper.selectCount(null));
        indexStat.setDepartmentNumber(departmentMapper.selectCount(null));
        indexStat.setCurrentCreateUser(userMapper.currentDayRegUser());
        indexStat.setApiCall(baseMapper.totalApiCall());
        indexStat.setCurrentApiCall(baseMapper.currentDayApiCall());
        indexStat.setCurrentUserLogin(baseMapper.currentLoginNumber());
        return ResponseData.getSuccessInstance(indexStat);
    }

    @Async
    @Override
    public void createLoginLog() {
        ShiroUser user = SecurityUtil.getCurrentUser();
        SysLog sysLog = new SysLog();
        sysLog.setType(0);
        sysLog.setTitle("????????????");
        sysLog.setContent(user.getAccount() + "????????????");
        sysLog.setCreateTime(new Date());
        sysLog.setCreateUserId(user.getId());
        sysLog.setRemoteAddr(ServletUtil.getClientIP(WebUtil.getRequest()));
        baseMapper.insert(sysLog);
    }

    private void pass(ResponseData responseData, SysLog log) {
        insertCommonInfo(log);
        Long id = logPersistStrategy.persist(log);
        responseData.setCode(Const.CODE_SUCCESS);
        responseData.setMsg(Const.CODE_SUCCESS_STR);
        responseData.setData(id);
    }
}
