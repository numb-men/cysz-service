package cn.hengyumo.humor.wx.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * WxUserInfoDto
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/14
 */
@Data
public class WxUserInfoDto {

    private String openId;

    @NotBlank
    private String avatarUrl;

    @NotBlank
    private String city;

    @NotBlank
    private String country;

    @NotNull
    private Integer gender;

    @NotBlank
    private String language;

    @NotBlank
    private String nickName;

    @NotBlank
    private String province;
}
