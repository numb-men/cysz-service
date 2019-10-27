package cn.hengyumo.humor.wx.exception.common;


import cn.hengyumo.humor.base.exception.common.BaseException;
import cn.hengyumo.humor.wx.exception.enums.WxUserResultEnum;

/**
 * WxUserException
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/15
 */
public class WxUserException extends BaseException {
    public WxUserException(WxUserResultEnum wxUserResultEnum) {
        super(wxUserResultEnum);
    }
}
