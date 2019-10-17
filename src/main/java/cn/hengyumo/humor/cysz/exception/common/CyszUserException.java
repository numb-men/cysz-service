package cn.hengyumo.humor.cysz.exception.common;

import cn.hengyumo.humor.base.exception.common.BaseException;
import cn.hengyumo.humor.cysz.exception.enums.CyszUserResultEnum;

/**
 * CyszUserException
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/10/12
 */
public class CyszUserException extends BaseException {
    public CyszUserException(CyszUserResultEnum cyszUserResultEnum) {
        super(cyszUserResultEnum);
    }
}
