package com.jcloud.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author jiaxm
 * @date 2021/11/11
 */
@Controller
public class HelloController {

    @RequestMapping("hi")
    public String hello() {
        return "/hello-world";
    }
}
