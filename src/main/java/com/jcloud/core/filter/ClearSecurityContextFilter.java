package com.jcloud.core.filter;

import com.jcloud.utils.SecurityUtil;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author jiaxm
 * @date 2021/11/11
 */
@Component
public class ClearSecurityContextFilter implements Filter, Ordered {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(servletRequest, servletResponse);
        SecurityUtil.removeCurrentUser();
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
