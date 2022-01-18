package com.jcloud.core.service;

import cn.hutool.core.io.IoUtil;
import com.jcloud.bean.EncryptionBean;
import com.jcloud.utils.AES;
import com.jcloud.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * @author jiaxm
 * @date 2022/1/10
 */
@Slf4j
public class DecryptHttpInputMessage implements HttpInputMessage {

    private HttpInputMessage inputMessage;

    private String password;

    public DecryptHttpInputMessage(HttpInputMessage httpInputMessage, String password) {
        this.inputMessage = httpInputMessage;
        this.password = password;
    }

    @Override
    public InputStream getBody() throws IOException {
        Charset charset = Charset.forName("UTF-8");
        String content = IoUtil.read(inputMessage.getBody(), charset);
        EncryptionBean bean = JsonUtils.readObject(content, EncryptionBean.class);
        byte[] bytes = null;
        try {
            bytes = AES.getInstance().decrypt(bean.getData(), password).getBytes(charset);
        } catch (Exception e) {
            log.warn("解密错误，实用原本内容！");
            bytes = content.getBytes(charset);
        }
        return new ByteArrayInputStream(bytes);
    }

    @Override
    public HttpHeaders getHeaders() {
        return inputMessage.getHeaders();
    }
}
