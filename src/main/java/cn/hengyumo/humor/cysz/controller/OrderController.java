package cn.hengyumo.humor.cysz.controller;

import cn.hengyumo.humor.base.annotation.SystemUserAuth;
import cn.hengyumo.humor.base.mvc.BaseController;
import cn.hengyumo.humor.base.result.Result;
import cn.hengyumo.humor.cysz.base.CyszUserAuth;
import cn.hengyumo.humor.cysz.dto.OrderItemDto;
import cn.hengyumo.humor.cysz.dto.OrderSearchDto;
import cn.hengyumo.humor.cysz.entity.Order;
import cn.hengyumo.humor.cysz.service.OrderService;
import cn.hengyumo.humor.system.annotation.SystemResource;
import cn.hengyumo.humor.system.annotation.SystemResourceClass;
import cn.hengyumo.humor.system.config.SystemConfig;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * OrderController
 * 串意十足订单 接口
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/10/12
 */

@Validated
@RestController
@RequestMapping("/cysz/order")
@SystemResourceClass(resourceName = "order", comment = "订单",
        parentResource = SystemConfig.DEFAULT_RESOURCE_PARENT_CODE)
public class OrderController extends BaseController<OrderService, Order, Long> {

    @CyszUserAuth
    @PostMapping("/create")
    @SystemResource(comment = "创建订单")
    public Result createOrder(@Valid @RequestBody List<OrderItemDto> orderItemDtos) {
        return Result.success(baseService.createOrder(orderItemDtos));
    }

    @CyszUserAuth
    @GetMapping("/u/page")
    @SystemResource(comment = "前端用户获取订单分页")
    public Result findAllPagedU(@RequestParam int pageNumber, @RequestParam int pageSize) {
        return Result.success(baseService.userFindAllPage(pageNumber, pageSize));
    }

    @CyszUserAuth
    @GetMapping("/refund")
    @SystemResource(comment = "退款")
    public Result refund(@RequestParam Long id) {
        return Result.success(baseService.refund(id));
    }

    @CyszUserAuth
    @GetMapping("/arrived")
    @SystemResource(comment = "确认送达")
    public Result arrived(@RequestParam Long id) {
        return Result.success(baseService.arrived(id));
    }

    @SystemUserAuth
    @PostMapping("/search")
    @SystemResource(comment = "后台查询订单并分页")
    public Result search(@RequestParam int pageNumber, @RequestParam int pageSize,
                         @RequestBody OrderSearchDto orderSearchDto) {
        return Result.success(baseService.systemSearchPaged(pageNumber, pageSize, orderSearchDto));
    }
}
