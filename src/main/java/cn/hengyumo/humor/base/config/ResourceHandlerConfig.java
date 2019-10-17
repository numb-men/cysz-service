package cn.hengyumo.humor.base.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * ResourceHandlerConfig
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/19
 */
@Configuration
public class ResourceHandlerConfig implements WebMvcConfigurer {

    @Value("${hengyumo.file.upload.path}")
    private String fileUploadPath;

    /**
     * Add handlers to serve static resources such as images, js, and, css
     * file from specific locations under web application root, the classpath,
     * and others.
     *
     * @param registry registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/media/**")
                .addResourceLocations("file:" + fileUploadPath);
    }
}
