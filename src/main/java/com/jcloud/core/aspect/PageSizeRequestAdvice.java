package com.jcloud.core.aspect;

import com.jcloud.core.config.SystemProperty;
import com.jcloud.utils.ReflectUtil;
import com.jcloud.utils.TypeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * 对body中的pageSize进行校验，配合filter使用
 *
 * @author jiaxm
 * @date 2022/1/14
 * @see ModifyPageSizeFilter
 */
@ControllerAdvice
public class PageSizeRequestAdvice implements RequestBodyAdvice {

    @Autowired
    private SystemProperty systemProperty;

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        return inputMessage;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 反射查看有无pageSize字段
        Field pageField = ReflectUtil.getFieldByFieldName(body, "pageSize");
        if (pageField != null) {
            // 获取pageSize值
            Object pageSize = ReflectUtil.getFieldValue(pageField, body);
            if (pageSize != null && pageSize instanceof Number) { // 只转换数字
                Number tmp = (Number) pageSize;
                if (tmp.longValue() >= systemProperty.getMaxPageSize()) { // 大于给定的设定值，设置为默认值
                    ReflectUtil.setFieldValue(pageField, body, TypeUtil.castType(tmp, systemProperty.getMaxPageSize()));
                }
            }
        }
        return body;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return null;
    }
}
