package cn.hengyumo.humor.utils.wx.exception.enums;


import cn.hengyumo.humor.base.result.ResultEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * WxRequestResultEnum
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/14
 */
@Getter
@AllArgsConstructor
public enum WxRequestResultEnum implements ResultEnum {

    WX_REQUEST_SUCCEED(0, "微信请求成功"),
    WX_REQUEST_FAIL(-2, "微信请求失败"),

    REQUEST_URI_INVALID(10099, "请求URI不规范"),
    NET_CONNECT_FAIL(10100, "网络无法连接"),

    /* 登录 */
    WX_SYSTEM_BUSY(-1, "微信系统繁忙，此时请开发者稍候再试"),
    WX_CODE_INVALID(40029, "微信登录code无效"),
    WX_CODE_HAS_BEEN_USED(40163, "微信code已经被用过"),
    WX_API_LIMIT(45011, "微信请求频率限制"),
    ;

    private Integer code;
    private String message;

    public static WxRequestResultEnum getByCode(Integer code) {
        for (WxRequestResultEnum wxRequestResultEnum : WxRequestResultEnum.values()) {
            if (wxRequestResultEnum.getCode().equals(code)) {
                return wxRequestResultEnum;
            }
        }
        return null;
    }
}
