package cn.hengyumo.humor.base.config;

import cn.hengyumo.humor.base.system.user.SystemCurrentUser;
import cn.hengyumo.humor.cysz.user.CyszCurrentUser;
import cn.hengyumo.humor.wx.user.WxCurrentUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * CurrentUserConfig
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/13
 */
@Configuration
public class CurrentUserConfig {

    @Bean
    @Scope("singleton")
    public SystemCurrentUser systemCurrentUser() {
        return new SystemCurrentUser();
    }

    @Bean
    @Scope("singleton")
    public WxCurrentUser wxCurrentUser() {
        return new WxCurrentUser();
    }

    @Bean
    @Scope("singleton")
    public CyszCurrentUser cyszCurrentUser() {
        return new CyszCurrentUser();
    }
}
