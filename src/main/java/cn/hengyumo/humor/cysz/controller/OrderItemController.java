package cn.hengyumo.humor.cysz.controller;

import cn.hengyumo.humor.base.mvc.BaseController;
import cn.hengyumo.humor.cysz.entity.OrderItem;
import cn.hengyumo.humor.cysz.service.OrderItemService;
import cn.hengyumo.humor.system.annotation.SystemResourceClass;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * OrderController
 * 串意十足订单项 接口
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
