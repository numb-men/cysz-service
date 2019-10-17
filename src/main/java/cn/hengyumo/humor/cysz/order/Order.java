package cn.hengyumo.humor.cysz.order;

import cn.hengyumo.humor.base.mvc.BaseEntity;
import cn.hengyumo.humor.base.system.config.SystemDict;
import cn.hengyumo.humor.base.system.config.SystemDictItem;
import cn.hengyumo.humor.base.system.config.UseSystemDict;
import cn.hengyumo.humor.base.system.dict.SystemDictDeserializer;
import cn.hengyumo.humor.base.system.dict.SystemDictSerializer;
import cn.hengyumo.humor.cysz.order.item.OrderItem;
import cn.hengyumo.humor.cysz.user.CyszUser;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

/**
 * Order
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/10/12
 */
@Data
@Cacheable
@UseSystemDict
@Entity(name = "tb_cysz_order")
@EqualsAndHashCode(callSuper = true)
public class Order extends BaseEntity<Long> {

    private Double allPrice;

    @SystemDict(name = "orderStatus", comment = "订单状态", items = {
            @SystemDictItem(value = "订单配送中"),
            @SystemDictItem(value = "订单已送达"),
            @SystemDictItem(value = "订单已退款")
    })
    @JsonDeserialize(using = SystemDictDeserializer.class)
    @JsonSerialize(using = SystemDictSerializer.class)
    private Integer status;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cysz_user_id", referencedColumnName = "id")
    private CyszUser cyszUser;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "tb_cysz_order_order_item", joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "order_item_id"))
    private List<OrderItem> orderItems;
}
