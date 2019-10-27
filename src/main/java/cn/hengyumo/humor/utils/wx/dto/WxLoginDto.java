package cn.hengyumo.humor.utils.wx.dto;

import lombok.Data;

/**
 * WxLoginDto
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/14
 */
@Data
public class WxLoginDto {

    private String appid;

    private String secret;

    private String js_code;

    private final String grant_type = "authorization_code";
}
