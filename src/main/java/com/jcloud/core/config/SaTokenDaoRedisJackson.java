package com.jcloud.core.config;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.util.SaFoxUtil;
import com.jcloud.bean.ShiroUser;
import com.jcloud.consts.Const;
import com.jcloud.utils.SecurityUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author jiaxm
 * @date 2021/11/11
 */
@Component
public class SaTokenDaoRedisJackson implements SaTokenDao {

    @Autowired
    private RedisService redisService;


    public SaTokenDaoRedisJackson() {
    }


    public String get(String key) {
        String prefix = SaManager.getConfig().getTokenName() + ":login:token:";
        String id = (String)this.redisService.stringTemplate().opsForValue().get(key);
        if (key.startsWith(prefix) && SecurityUtil.getCurrentUser() == null && StringUtils.isNotBlank(id)) {
            ShiroUser shiroUser = (ShiroUser) redisService.get(Const.USER_INFO_KEY + id);
            SecurityUtil.setCurrentUser(shiroUser);
        }
        return id;
    }

    public void set(String key, String value, long timeout) {
        String prefix = SaManager.getConfig().getTokenName() + ":login:token:";
        if (timeout != 0L && timeout > -2L) {
            if (timeout == -1L) {
                this.redisService.stringTemplate().opsForValue().set(key, value);
            } else {
                this.redisService.stringTemplate().opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
            }
        }
        if (key.startsWith(prefix) && SecurityUtil.getCurrentUser() != null) {
            this.redisService.template().opsForValue().set(Const.USER_INFO_KEY + value, SecurityUtil.getCurrentUser(), SaManager.getConfig().getIdTokenTimeout(), TimeUnit.SECONDS);
        }
    }

    public void update(String key, String value) {
        long expire = this.getTimeout(key);
        if (expire != -2L) {
            this.set(key, value, expire);
        }
    }

    public void delete(String key) {
        this.redisService.stringTemplate().delete(key);
    }

    public long getTimeout(String key) {
        return this.redisService.stringTemplate().getExpire(key);
    }

    public void updateTimeout(String key, long timeout) {
        if (timeout == -1L) {
            long expire = this.getTimeout(key);
            if (expire != -1L) {
                this.set(key, this.get(key), timeout);
            }

        } else {
            this.redisService.stringTemplate().expire(key, timeout, TimeUnit.SECONDS);
        }
    }

    public Object getObject(String key) {
        return this.redisService.get(key);
    }

    public void setObject(String key, Object object, long timeout) {
        if (timeout != 0L && timeout > -2L) {
            if (timeout == -1L) {
                this.redisService.set(key, object);
            } else {
                this.redisService.set(key, object, timeout);
            }

        }
    }

    public void updateObject(String key, Object object) {
        long expire = this.getObjectTimeout(key);
        if (expire != -2L) {
            this.setObject(key, object, expire);
        }
    }

    public void deleteObject(String key) {
        this.redisService.del(key);
    }

    public long getObjectTimeout(String key) {
        return this.redisService.template().getExpire(key);
    }

    public void updateObjectTimeout(String key, long timeout) {
        if (timeout == -1L) {
            long expire = this.getObjectTimeout(key);
            if (expire != -1L) {
                this.setObject(key, this.getObject(key), timeout);
            }

        } else {
            this.redisService.template().expire(key, timeout, TimeUnit.SECONDS);
        }
    }

    public List<String> searchData(String prefix, String keyword, int start, int size) {
        Set<String> keys = this.redisService.stringTemplate().keys(prefix + "*" + keyword + "*");
        List<String> list = new ArrayList(keys);
        return SaFoxUtil.searchList(list, start, size);
    }
}
