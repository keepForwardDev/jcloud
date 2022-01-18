package com.jcloud.core.aspect;

import cn.hutool.extra.spring.SpringUtil;
import com.jcloud.core.service.RequestEncryption;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 需要加密的接口进行加密
 * 用于数据脱敏
 * @author jiaxm
 * @date 2022/1/10
 */
@ControllerAdvice
public class EncryptResponseBodyAdvice implements ResponseBodyAdvice {
    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        DecryptRequest request = methodParameter.getMethod().getAnnotation(DecryptRequest.class);
        return request != null && request.enableEncrypt();
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        DecryptRequest request = methodParameter.getMethod().getAnnotation(DecryptRequest.class);
        RequestEncryption requestEncryption = SpringUtil.getBean(request.convertClazz());
        return requestEncryption.encrypt(o);
    }
}
