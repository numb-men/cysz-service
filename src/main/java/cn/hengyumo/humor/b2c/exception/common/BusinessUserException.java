package cn.hengyumo.humor.b2c.exception.common;

import cn.hengyumo.humor.b2c.exception.enums.BusinessUserResultEnum;
import cn.hengyumo.humor.base.exception.common.BaseException;

/**
 * BusinessUserException
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/16
 */
public class BusinessUserException extends BaseException {
    public BusinessUserException(BusinessUserResultEnum businessUserResultEnum) {
        super(businessUserResultEnum);
    }
}
