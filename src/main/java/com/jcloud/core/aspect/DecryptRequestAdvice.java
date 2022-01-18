package com.jcloud.core.aspect;

import com.jcloud.core.config.SystemProperty;
import com.jcloud.core.service.DecryptHttpInputMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * 对传入的数据进行解密，然后绑定到body上
 *
 * @author jiaxm
 * @date 2022/1/10
 */
@ControllerAdvice
public class DecryptRequestAdvice implements RequestBodyAdvice {

    @Autowired
    private SystemProperty systemProperty;

    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        DecryptRequest request = methodParameter.getMethod().getAnnotation(DecryptRequest.class);
        return request != null && request.enableDecrypt();
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) throws IOException {
        return new DecryptHttpInputMessage(httpInputMessage, systemProperty.getEncryptorPassword());
    }

    @Override
    public Object afterBodyRead(Object o, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return o;
    }

    @Override
    public Object handleEmptyBody(Object o, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return null;
    }
}
