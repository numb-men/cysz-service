package cn.hengyumo.humor.cysz.order.item;

import cn.hengyumo.humor.base.mvc.BaseController;
import cn.hengyumo.humor.base.system.config.SystemResourceClass;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * OrderController
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/10/12
 */

@Validated
@RestController
@RequestMapping("/cysz/order/item")
@SystemResourceClass(resourceName = "orderItem", comment = "订单项", parentResource = "order")
public class OrderItemController extends BaseController<OrderItemService, OrderItem, Long> {
}
