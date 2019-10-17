package cn.hengyumo.humor.cysz.exception.enums;

import cn.hengyumo.humor.base.result.ResultEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * FoodResultEnum
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/10/12
 */
@Getter
@AllArgsConstructor
public enum  FoodResultEnum implements ResultEnum {
    FOOD_NOT_FOUND(4101, "菜品不存在")
    ;

    private Integer code;
    private String message;
}
