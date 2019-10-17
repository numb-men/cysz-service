package cn.hengyumo.humor.base.config;


import cn.hengyumo.humor.base.interceptor.AuthenticationInterceptor;
import cn.hengyumo.humor.base.interceptor.ParamNotBlankInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * InterceptorConfig
 * 配置处理token、NotNull
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/4/25
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor());
        registry.addInterceptor(notNullInterceptor())
                .addPathPatterns("/**");
    }

    @Bean
    public AuthenticationInterceptor authenticationInterceptor() {
        return new AuthenticationInterceptor();
    }

    @Bean
    public ParamNotBlankInterceptor notNullInterceptor() {
        return new ParamNotBlankInterceptor();
    }
}
