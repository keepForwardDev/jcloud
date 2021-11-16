package com.jcloud.core.config;

import cn.dev33.satoken.interceptor.SaAnnotationInterceptor;
import cn.dev33.satoken.interceptor.SaRouteInterceptor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import com.jcloud.consts.Const;
import com.jcloud.core.service.LongToStringSerializer;
import com.jcloud.core.support.DictionaryBeanSerializerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.Charset;
import java.util.List;

@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private SystemProperty systemProperty;



    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        StringHttpMessageConverter converter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        return converter;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(responseBodyConverter());
    }

    /**
     * 注册过滤器
     * 解决中文乱码
     * @return
     */
    @Bean
    public FilterRegistrationBean initializeCharacterEncodingFilter() {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        FilterRegistrationBean registration = new FilterRegistrationBean(filter);
        registration.addUrlPatterns("/**");
        return registration;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(Const.FILE_VISIT_PREFIX + "/**")
        .addResourceLocations("file:"+ systemProperty.getExtPath() + "/");

    }


    @Primary
    @Bean
    public ObjectMapper objectMapper (Jackson2ObjectMapperBuilder builder) {
        // 设置objectMapper 支持 字典转换
        DictionaryBeanSerializerFactory serializerFactory = new DictionaryBeanSerializerFactory(null);
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        SimpleModule simpleModule = new SimpleModule();
        SimpleSerializers simpleSerializers = new SimpleSerializers();
        LongToStringSerializer longToStringSerializer = new LongToStringSerializer(Long.class);
        simpleModule.addSerializer(Long.class, longToStringSerializer);
        simpleModule.addSerializer(Long.TYPE, longToStringSerializer);
        simpleSerializers.addSerializer(Long.class, longToStringSerializer);
        simpleSerializers.addSerializer(Long.TYPE, longToStringSerializer);
        objectMapper.registerModule(simpleModule);
        objectMapper.setSerializerFactory(serializerFactory.withAdditionalSerializers(simpleSerializers));
        return objectMapper;
    }

    // 注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册Sa-Token的路由拦截器
        PatternMacher patternMacher = new PatternMacher();
        registry.addInterceptor(new SaRouteInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(systemProperty.getNotAuthUrls()).pathMatcher(patternMacher);
        registry.addInterceptor(new SaAnnotationInterceptor()).addPathPatterns("/**").pathMatcher(patternMacher);
    }

    //    /**
//     * 所有空输出json变为 empty String
//     * @param builder
//     * @return
//     */
//    @Bean
//    @Primary
//    @ConditionalOnMissingBean(ObjectMapper.class)
//    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
//        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
//        objectMapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
//            @Override
//            public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
//                jsonGenerator.writeString("");
//            }
//        });
//        return objectMapper;
//    }
}
