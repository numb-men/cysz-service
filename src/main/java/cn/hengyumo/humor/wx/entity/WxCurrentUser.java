package cn.hengyumo.humor.wx.entity;

import lombok.Data;

/**
 * WxCurrentUser
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/14
 */
@Data
public class WxCurrentUser {

    private Long id;
    private String wxOpenId;
    private String wxSessionKey;
    private WxUser wxUser;
}
