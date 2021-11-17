package com.jcloud.core.config;

import com.jcloud.bean.ShiroUser;
import com.jcloud.utils.SecurityUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.task.TaskExecutorCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * spring 默认调度池配置
 *
 * @author jiaxm
 * @date 2021/8/3
 */
@Configuration
@EnableAsync
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ThreadPoolConfig {

    /**
     * 默认策略为 AbortPolicy 不接受任务抛出异常
     * 使用CallerRunsPolicy 阻塞主线程执行
     *
     * @return
     */
    @Bean
    public TaskExecutorCustomizer rejectTaskExecutorCustomizer() {
        return (taskExecutor) -> {
            taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        };
    }

    /**
     * 解决异步登录问题
     *
     * @return
     */
    @Bean
    public TaskDecorator taskDecorator() {
        return (runnable -> {
            ShiroUser shiroUser = SecurityUtil.getCurrentUser();
            RequestAttributes context = RequestContextHolder.getRequestAttributes();
            return () -> {
                try {
                    RequestContextHolder.setRequestAttributes(context);
                    SecurityUtil.setCurrentUser(shiroUser);
                    runnable.run();
                } finally {
                    RequestContextHolder.resetRequestAttributes();
                    SecurityUtil.removeCurrentUser();
                }
            };
        });
    }

}
