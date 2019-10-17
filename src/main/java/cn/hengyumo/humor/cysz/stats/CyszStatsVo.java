package cn.hengyumo.humor.cysz.stats;

import lombok.Data;

/**
 * CyszStatsVo
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/10/13
 */
@Data
public class CyszStatsVo {

    private Long userAddNumToday;
    private Long clickAddNumToday;
    private Long orderAddNumToday;
    private Long orderNumAll;
    private Double earningsToday;
    private Double earningsAll;


}
