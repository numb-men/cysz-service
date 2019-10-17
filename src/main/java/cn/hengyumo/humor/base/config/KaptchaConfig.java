package cn.hengyumo.humor.base.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * CaptchaConfig
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/20
 */
@Configuration
public class KaptchaConfig {
    @Bean
    public DefaultKaptcha producer() {
        Properties properties = new Properties();
        properties.put("kaptcha.border", "no");
        properties.put("kaptcha.textproducer.font.color", "blue");
        properties.put("kaptcha.textproducer.char.space", "10");
        properties.put("kaptcha.textproducer.char.length", "4");
        properties.put("kaptcha.image.height", "34");
        properties.put("kaptcha.textproducer.font.size", "28");
        properties.put("kaptcha.noise.impl", "com.google.code.kaptcha.impl.DefaultNoise");
        properties.put("kaptcha.noise.color", "black");
        properties.put("kaptcha.obscurificator.impl", "com.google.code.kaptcha.impl.WaterRipple");
        properties.put("kaptcha.background.clear.from", "255,255,255");
        properties.put("kaptcha.background.clear.to", "white");
        // properties.put("kaptcha.textproducer.char.string",
        //         "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");

        Config config = new Config(properties);
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
