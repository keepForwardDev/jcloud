package com.jcloud.core.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import com.jcloud.consts.Const;
import com.jcloud.core.domain.ResponseData;
import com.jcloud.utils.JsonUtils;
import com.jcloud.utils.TypeUtil;
import com.jcloud.utils.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 全局异常处理
 *
 * @author jiaxm
 * @date 2021/3/30
 */
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ControllerAdvice
@Order
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseData globalExceptionResponse(HttpServletRequest request, Exception e, HttpServletResponse response) throws IOException {
        Map<String, Object> paramMap = new HashMap<>();
        Map<String, Object> headerMap = new HashMap<>();
        Enumeration enumeration = request.getParameterNames();
        while (enumeration.hasMoreElements()) {
            String paramKey = TypeUtil.toStr(enumeration.nextElement());
            String value = request.getParameter(paramKey);
            if (StringUtils.isBlank(value)) {
                LinkedHashMap jsonMap = JsonUtils.readObject(paramKey, LinkedHashMap.class);
                paramMap.put("传递的JSON数据", jsonMap);
            } else {
                paramMap.put(paramKey, value);
            }
        }
        Enumeration headers = request.getHeaderNames();
        while (headers.hasMoreElements()) {
            String headerKey = TypeUtil.toStr(headers.nextElement());
            String value = request.getHeader(headerKey);
            headerMap.put(headerKey, value);
        }
        log.error("请求错误：{} {} header:{} params:{}", request.getRequestURI(), request.getMethod(), headerMap.toString(), paramMap.toString());
        log.error(ExceptionUtils.getStackTrace(e));
        WebUtil.jsonResponseMIME(response);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        ResponseData commonRespon = new ResponseData();
        commonRespon.setMsg(String.format("%s [%s]", Const.INTERNAL_ERROR, e.getMessage()));
        return commonRespon;
    }


    @ExceptionHandler(value = NotPermissionException.class)
    @ResponseBody
    public ResponseData notPermissionException(NotPermissionException e) {
        ResponseData commonRespon = new ResponseData();
        commonRespon.setData("无权访问！");
        return commonRespon;
    }

    @ExceptionHandler(value = NotLoginException.class)
    @ResponseBody
    public ResponseData notLoginException(NotLoginException e) {
        ResponseData commonRespon = new ResponseData();
        commonRespon.setCode(Const.CODE_NO_LOGIN);
        commonRespon.setData(Const.CODE_NO_LOGIN_STR);
        return commonRespon;
    }
}
