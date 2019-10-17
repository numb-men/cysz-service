package cn.hengyumo.humor.cysz.exception.enums;

import cn.hengyumo.humor.base.result.ResultEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * CyszUserResultEnum
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/10/12
 */
@Getter
@AllArgsConstructor
public enum CyszUserResultEnum implements ResultEnum {
    MOBILE_INVALID(4001, "手机号码格式错误"),
    MOBILE_EXISTS(4002, "手机号码已被注册"),
    PASSWORD_ERROR(4003, "密码错误"),
    USERNAME_EXISTS(4004, "用户名已被注册")
    ;

    private Integer code;
    private String message;
}
