package cn.hengyumo.humor.cysz.order.item;

import cn.hengyumo.humor.base.mvc.BaseEntity;
import cn.hengyumo.humor.base.system.config.UseSystemDict;
import cn.hengyumo.humor.cysz.food.Food;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

/**
 * OrderItem
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/10/12
 */

@Data
@Cacheable
@UseSystemDict
@Entity(name = "tb_cysz_order_item")
@EqualsAndHashCode(callSuper = true)
public class OrderItem extends BaseEntity<Long> {

    @OneToOne(fetch = FetchType.EAGER)
    private Food food;

    private Integer num;

    private Double price;
}
