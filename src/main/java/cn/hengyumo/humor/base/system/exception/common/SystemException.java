package cn.hengyumo.humor.base.system.exception.common;


import cn.hengyumo.humor.base.exception.common.BaseException;
import cn.hengyumo.humor.base.system.exception.enums.SystemResultEnum;

/**
 * SystemException
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/11
 */
public class SystemException extends BaseException {
    public SystemException(SystemResultEnum systemResultEnum) {
        super(systemResultEnum);
    }
}
