package cn.hengyumo.humor.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * Captcha
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/20
 */
@Data
public class Captcha {

    private String captchaToken;

    @JsonIgnore
    private String captcha;

    private Integer expire;

    private String image;
}
