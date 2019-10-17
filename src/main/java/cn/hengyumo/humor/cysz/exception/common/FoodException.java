package cn.hengyumo.humor.cysz.exception.common;

import cn.hengyumo.humor.base.exception.common.BaseException;
import cn.hengyumo.humor.cysz.exception.enums.FoodResultEnum;

/**
 * FoodException
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/10/12
 */
public class FoodException extends BaseException {
    public FoodException(FoodResultEnum foodResultEnum) {
        super(foodResultEnum);
    }
}
