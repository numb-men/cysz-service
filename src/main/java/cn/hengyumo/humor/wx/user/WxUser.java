package cn.hengyumo.humor.wx.user;


import cn.hengyumo.humor.b2c.entity.B2cBaseUser;
import cn.hengyumo.humor.base.mvc.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * WxUser
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/14
 */
@Data
@Entity(name = "tb_wx_user")
@EqualsAndHashCode(callSuper = true)
public class WxUser extends BaseEntity<Long> {

    @NotBlank
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

    @OneToOne(fetch = FetchType.LAZY)
    private B2cBaseUser b2cBaseUser;
}
