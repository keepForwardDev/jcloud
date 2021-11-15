package com.jcloud.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.jcloud.consts.LoginType;
import com.jcloud.consts.SecurityConstants;
import com.jcloud.core.domain.ResponseData;
import com.jcloud.service.UserService;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

/**
 * @author jiaxm
 * @date 2021/11/12
 */
@RequestMapping("auth")
@Controller
public class LoginController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserService userService;


    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData doLogin(@ApiParam(value = "用户名") String username, @ApiParam(value = "密码") String password, @ApiParam(value = "验证码") String code, @ApiParam(value = "是否记住密码") Integer rememberme, @ApiParam(value = "是否同步返回用户信息") Integer info) {
        return userService.doLogin(username, password, code, LoginType.ACCOUNT_NAME, rememberme, info);
    }

    @RequestMapping(value = "createCode", method = RequestMethod.GET)
    public void createCode(HttpServletResponse response, String requestSn) throws Exception {
        if (StringUtils.isBlank(requestSn)) {
            return;
        }
        //设置response响应
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 100,4,150);
        stringRedisTemplate.opsForValue().set(requestSn, lineCaptcha.getCode(), SecurityConstants.CODE_EXPIRED_TIME, TimeUnit.MINUTES);
        OutputStream out= response.getOutputStream();
        lineCaptcha.write(out);
        out.flush();
    }

    @RequestMapping("logout")
    @ResponseBody
    public ResponseData logout(String token) {
        StpUtil.logout();
        return ResponseData.getSuccessInstance();
    }
}
