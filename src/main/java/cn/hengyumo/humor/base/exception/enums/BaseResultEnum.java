package cn.hengyumo.humor.base.exception.enums;


import cn.hengyumo.humor.base.result.ResultEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * BaseResultEnum
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/9
 */
@Getter
@AllArgsConstructor
public enum BaseResultEnum implements ResultEnum {
    SUCCESS(200, "请求成功"),
    FAIL(400, "请求失败"),

    UNKNOWN_ERROR(1001, "未知错误"),
    NOT_LOGIN(1002, "未登录"),
    USER_NOT_FIND(1003, "用户不存在"),
    TOKEN_CHECK_FAILED(1004, "登录凭证校验失败"),
    NOT_POWER(1005, "没有权限"),
    HAS_REGISTERED(1006, "该用户名已注册"),
    NOT_USER(1007, "用户不存在"),
    PASSWORD_ERROR(1008, "密码错误"),
    NOT_GOOD_PASSWORD(1009, "密码应由8-12位数字和字母组成"),
    PASSWORD_EQUAL(1010, "新密码和旧密码相同，不改变"),
    SET_AUDITOR_FAIL(1011, "设置用户失败"),

    UN_MAPPING_JSON(704, "无法映射的JSON"),
    CAPTCHA_EXPIRE(705, "验证码过期"),
    CAPTCHA_ERROR(706, "验证码错误")
    ;

    private Integer code;
    private String message;
}
