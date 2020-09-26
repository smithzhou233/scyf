package com.hngf.api.common.config;

import com.hngf.api.common.annotation.ApiVersion;
import com.hngf.api.common.condition.ApiVersionCondition;
import com.hngf.api.common.interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 拦截器配置
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    @Value("${scyf.uploaddir}")
    private String uploadDir;
    @Value("${scyf.accessPath}")
    private String accessPath;

    @Autowired
    private TokenInterceptor tokenInterceptor;

    /**
     * 重写父类方法：将返回json为null的情况特殊处理
     * @param converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        //在json转换之前先进行string转换
        converters.add(new StringHttpMessageConverter());
        //添加json转换
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        jackson2HttpMessageConverter.setObjectMapper(new MyJsonMapper());
        converters.add(jackson2HttpMessageConverter);
        //追加默认转换器
        super.addDefaultHttpMessageConverters(converters);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截所有以api开头的请求
        registry.addInterceptor(tokenInterceptor).addPathPatterns("/api/**");
    }

    /**
     * 如果继承了WebMvcConfigurationSupport，则在yml中配置的相关内容会失效。 需要重新指定静态资源
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //Swagger2文档接口静态资源放行
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler(accessPath).addResourceLocations("file:" + uploadDir);
        super.addResourceHandlers(registry);
    }

    /**
     * 版本号匹配拦截器，处理具有ApiVersion注解的url
     * @return
     */
    @Override
    protected RequestMappingHandlerMapping createRequestMappingHandlerMapping() {
        return new ApiVersioningRequestMappingHandlerMapping();
    }

    private class ApiVersioningRequestMappingHandlerMapping extends RequestMappingHandlerMapping {
        @Override
        protected RequestCondition<ApiVersionCondition> getCustomTypeCondition(Class<?> handlerType) {
            ApiVersion apiVersion = AnnotationUtils.findAnnotation(handlerType, ApiVersion.class);
            return createCondition(apiVersion);
        }

        @Override
        protected RequestCondition<ApiVersionCondition> getCustomMethodCondition(Method method) {
            ApiVersion apiVersion = AnnotationUtils.findAnnotation(method, ApiVersion.class);
            return createCondition(apiVersion);
        }

        private RequestCondition<ApiVersionCondition> createCondition(ApiVersion apiVersion) {
            return apiVersion == null ? null : new ApiVersionCondition(apiVersion.value());
        }
    }

}
