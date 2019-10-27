package cn.hengyumo.humor.cysz.entity;

import cn.hengyumo.humor.base.mvc.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import java.util.Date;

/**
 * CyszDailyReq
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/10/13
 */
@Data
@Entity(name = "tb_cysz_stats_daily_req")
@EqualsAndHashCode(callSuper = true)
public class CyszDailyReq extends BaseEntity<Long> {

    private Date date;

    private Long reqNum;
}
