package cn.hengyumo.humor.system.service;

import cn.hengyumo.humor.base.cache.EhcacheCache;
import cn.hengyumo.humor.system.entity.Captcha;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

/**
 * CaptchaService
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/20
 */
@Service
public class CaptchaService {

    @Value("${hengyumo.base.system.captcha.expire}")
    private Integer captchaExpire;

    @Resource
    private DefaultKaptcha producer;


    public Captcha createCaptcha() {
        // 生成文字验证码
        String text = producer.createText();
        // 生成图片验证码
        BufferedImage image = producer.createImage(text);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Captcha captcha = new Captcha();
        String captchaToken = UUID.randomUUID().toString();
        EhcacheCache.setValue("captchaCache", captchaToken, text, captchaExpire);
        captcha.setCaptcha(text);
        captcha.setCaptchaToken(captchaToken);
        captcha.setExpire(captchaExpire);
        // 对字节数组Base64编码
        captcha.setImage("data:image/jpeg;base64," +
                Base64.getEncoder().encodeToString(outputStream.toByteArray()));
        return captcha;
    }
}
