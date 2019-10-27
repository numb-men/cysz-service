package cn.hengyumo.humor.utils.wx.exception.common;


import cn.hengyumo.humor.base.exception.common.BaseException;
import cn.hengyumo.humor.utils.wx.exception.enums.WxRequestResultEnum;
import lombok.Getter;

/**
 * WxRequestException
 * TODO
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/14
 */
@Getter
public class WxRequestException extends BaseException {

    public WxRequestException(WxRequestResultEnum wxRequestResultEnum) {
        super(wxRequestResultEnum);
    }
}
