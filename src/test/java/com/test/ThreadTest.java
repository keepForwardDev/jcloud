package com.test;

import com.jcloud.utils.SecurityUtil;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * @author jiaxm
 * @date 2021/11/17
 */
public class ThreadTest {
    public static void main(String[] args) {
        RequestContextHolder.getRequestAttributes();
    }
}
