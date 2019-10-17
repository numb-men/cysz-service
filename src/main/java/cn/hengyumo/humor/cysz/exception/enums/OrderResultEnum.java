package cn.hengyumo.humor.cysz.exception.enums;

import cn.hengyumo.humor.base.result.ResultEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * OrderResultEnum
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/10/12
 */
@Getter
@AllArgsConstructor
public enum  OrderResultEnum implements ResultEnum {
    ORDER_IS_EMPTY(4201, "订单是空的"),
    BALANCE_NOT_ENOUGH(4202, "余额不足"),
    ORDER_NOT_FOUND(4203, "订单不存在"),
    NOT_YOU_ORDER(4204, "无权操作该订单"),
    ORDER_HAS_END(4205, "该订单已结束")
    ;

    private Integer code;
    private String message;
}
