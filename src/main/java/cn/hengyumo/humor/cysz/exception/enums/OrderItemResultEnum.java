package cn.hengyumo.humor.cysz.exception.enums;

import cn.hengyumo.humor.base.result.ResultEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * OrderItemResultEnum
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/10/12
 */
@Getter
@AllArgsConstructor
public enum  OrderItemResultEnum implements ResultEnum {
    ;

    private Integer code;
    private String message;
}
