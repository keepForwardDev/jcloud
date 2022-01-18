package com.jcloud.core.filter;

import com.jcloud.core.config.SystemProperty;
import com.jcloud.core.service.ModifyPageSizeRequestWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用于保护服务资源，我们将pageSize交给用户进行输入，这样很可能造成OOM，所以设置最大值保护， 配合body使用
 *
 * @author jiaxm
 * @date 2022/1/13
 * @see PageSizeRequestAdvice
 */
@Component
public class ModifyPageSizeFilter extends OncePerRequestFilter implements Ordered {

    @Autowired
    private SystemProperty systemProperty;


    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        filterChain.doFilter(new ModifyPageSizeRequestWrapper(request, systemProperty.getMaxPageSize()), response);
    }

}
