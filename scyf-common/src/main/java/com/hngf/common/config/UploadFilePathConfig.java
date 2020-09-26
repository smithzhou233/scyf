package com.hngf.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class UploadFilePathConfig implements WebMvcConfigurer {
    @Value("${scyf.uploaddir}")
    private String uploadDir;
    @Value("${scyf.accessPath}")
    private String accessPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
     registry.addResourceHandler(accessPath).addResourceLocations("file:" + uploadDir);
    }

}
