package cn.hengyumo.humor.wx.utils.exception;


import cn.hengyumo.humor.base.exception.common.BaseException;
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
