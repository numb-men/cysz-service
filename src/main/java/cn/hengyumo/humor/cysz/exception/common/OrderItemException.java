package cn.hengyumo.humor.cysz.exception.common;

import cn.hengyumo.humor.base.exception.common.BaseException;
import cn.hengyumo.humor.cysz.exception.enums.OrderItemResultEnum;

/**
 * OrderItemException
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/10/12
 */
public class OrderItemException extends BaseException {
    public OrderItemException(OrderItemResultEnum orderItemResultEnum) {
        super(orderItemResultEnum);
    }
}
