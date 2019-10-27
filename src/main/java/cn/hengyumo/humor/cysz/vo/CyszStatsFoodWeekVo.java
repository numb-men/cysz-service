package cn.hengyumo.humor.cysz.vo;

import lombok.Data;

/**
 * CyszStatsFoodWeekVo
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/10/15
 */
@Data
public class CyszStatsFoodWeekVo {

    private String foodName;
    private Long[] foodWeekSellNum;
}
