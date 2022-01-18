package com.jcloud.core.service;

import com.jcloud.bean.EncryptionBean;
import com.jcloud.consts.Const;
import com.jcloud.core.config.SystemProperty;
import com.jcloud.utils.AES;
import com.jcloud.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 默认加解密类
 * @author jiaxm
 * @date 2022/1/10
 */
@Service
public class DefaultJacksonEncryption implements RequestEncryption<EncryptionBean> {

    @Autowired
    private SystemProperty systemProperty;


    @Override
    public EncryptionBean encrypt(Object value) {
        EncryptionBean bean = new EncryptionBean();
        bean.setCode(Const.CODE_SUCCESS);
        try {
            bean.setData(AES.getInstance().encrypt(JsonUtils.toJsonString(value), systemProperty.getEncryptorPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bean;
    }

    @Override
    public Object decrypt(String value, Class clazz) {
        return JsonUtils.readObject(value, clazz);
    }
}
