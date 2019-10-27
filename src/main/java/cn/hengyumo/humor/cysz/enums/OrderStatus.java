package cn.hengyumo.humor.cysz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * OrderStatus
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/10/12
 */
@Getter
@AllArgsConstructor
public enum OrderStatus {
    IN_DELIVERY(1),
    HAS_ARRIVED(2),
    HAS_REFUND(3)
    ;

    private int code;
}
