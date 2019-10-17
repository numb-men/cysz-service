package cn.hengyumo.humor.cysz.order;

import cn.hengyumo.humor.base.exception.common.BaseException;
import cn.hengyumo.humor.base.exception.enums.BaseResultEnum;
import cn.hengyumo.humor.base.mvc.BaseService;
import cn.hengyumo.humor.base.utils.BaseUtil;
import cn.hengyumo.humor.cysz.exception.common.FoodException;
import cn.hengyumo.humor.cysz.exception.common.OrderException;
import cn.hengyumo.humor.cysz.exception.enums.FoodResultEnum;
import cn.hengyumo.humor.cysz.exception.enums.OrderResultEnum;
import cn.hengyumo.humor.cysz.food.Food;
import cn.hengyumo.humor.cysz.food.FoodService;
import cn.hengyumo.humor.cysz.order.item.OrderItem;
import cn.hengyumo.humor.cysz.order.item.OrderItemDto;
import cn.hengyumo.humor.cysz.order.item.OrderItemService;
import cn.hengyumo.humor.cysz.user.CyszCurrentUser;
import cn.hengyumo.humor.cysz.user.CyszUser;
import cn.hengyumo.humor.cysz.user.CyszUserService;
import com.github.wenhao.jpa.Specifications;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * OrderService
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/10/12
 */
@Slf4j
@Service
public class OrderService extends BaseService<Order, Long> {

    @Resource
    private FoodService foodService;

    @Resource
    private CyszUserService cyszUserService;

    @Resource
    private OrderDao orderDao;

    @Resource
    private BaseUtil baseUtil;


    @Transactional
    public Order createOrder(List<OrderItemDto> orderItemDtos) {
        if (orderItemDtos.size() > 0) {

            Order order = new Order();
            CyszUser cyszUser = cyszUserService.findOne(
                    baseUtil.getCyszCurrentUser().getId()).orElse(null);
            if (cyszUser == null) {
                throw new BaseException(BaseResultEnum.USER_NOT_FIND);
            }
            List<OrderItem> orderItems = new ArrayList<>();
            Double allPrice = 0D;
            for (OrderItemDto orderItemDto : orderItemDtos) {
                Food food = foodService.findOne(orderItemDto.getId()).orElse(null);
                if (food == null) {
                    throw new FoodException(FoodResultEnum.FOOD_NOT_FOUND);
                }
                Double sumPrice = food.getPrice() * orderItemDto.getNum();
                OrderItem orderItem = new OrderItem();
                orderItem.setFood(food);
                orderItem.setNum(orderItemDto.getNum());
                orderItem.setPrice(sumPrice);
                allPrice += sumPrice;
                // OrderItem orderItemSaved = orderItemService.save(orderItem);
                orderItems.add(orderItem);
            }

            // 余额不足
            if (cyszUser.getBalance() < allPrice) {
                throw new OrderException(OrderResultEnum.BALANCE_NOT_ENOUGH);
            }

            // 金额减去
            cyszUser.setBalance(cyszUser.getBalance() - allPrice);
            cyszUserService.save(cyszUser);

            order.setAllPrice(allPrice);
            order.setOrderItems(orderItems);
            order.setCyszUser(cyszUser);
            order.setStatus(OrderStatus.IN_DELIVERY.getCode());

            return save(order);
        }
        throw new OrderException(OrderResultEnum.ORDER_IS_EMPTY);
    }

    public Order arrived(Long id) {
        Order order = findOne(id).orElse(null);
        if (order == null) {
            throw new OrderException(OrderResultEnum.ORDER_NOT_FOUND);
        }
        if (order.getStatus() != OrderStatus.IN_DELIVERY.getCode()) {
            throw new OrderException(OrderResultEnum.ORDER_HAS_END);
        }
        if (baseUtil.currentUserIsCyszUser()) {
            CyszCurrentUser cyszCurrentUser = baseUtil.getCyszCurrentUser();
            if (! order.getCyszUser().getId().equals(cyszCurrentUser.getId())) {
                throw new OrderException(OrderResultEnum.NOT_YOU_ORDER);
            }
        }
        order.setStatus(OrderStatus.HAS_ARRIVED.getCode());
        return save(order);
    }

    public Order refund(Long id) {
        Order order = findOne(id).orElse(null);
        if (order == null) {
            throw new OrderException(OrderResultEnum.ORDER_NOT_FOUND);
        }
        if (order.getStatus() != OrderStatus.IN_DELIVERY.getCode()) {
            throw new OrderException(OrderResultEnum.ORDER_HAS_END);
        }
        CyszCurrentUser cyszCurrentUser = baseUtil.getCyszCurrentUser();

        if (baseUtil.currentUserIsCyszUser()) {
            if (!order.getCyszUser().getId().equals(cyszCurrentUser.getId())) {
                throw new OrderException(OrderResultEnum.NOT_YOU_ORDER);
            }
        }
        order.setStatus(OrderStatus.HAS_REFUND.getCode());
        CyszUser cyszUser = order.getCyszUser();
        cyszUser.setBalance(cyszUser.getBalance() + order.getAllPrice());
        cyszUserService.save(cyszUser);
        return save(order);
    }

    public Page<Order> userFindAllPage(Integer pageNumber, Integer pageSize) {
        CyszCurrentUser cyszCurrentUser = baseUtil.getCyszCurrentUser();
        if (cyszCurrentUser.getId() == null) {
            throw new BaseException(BaseResultEnum.USER_NOT_FIND);
        }
        Page<Order> page;
        // 只查询用户自身的订单
        Specification<Order> specification = Specifications.<Order>and()
                .eq("isDeleted", false)
                .eq("cyszUser.id", cyszCurrentUser.getId())
                .build();
        if (pageSize > 0) {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            page = findAll(specification, pageable);
        }
        else if (pageSize == 0) {
            page = new PageImpl<>(findAll());
        }
        else {
            page = new PageImpl<>(findAll(specification));
        }
        return page;
    }

    public Long countByCreateDateBetween(Date date1, Date date2) {
        return orderDao.countByCreateDateBetween(date1, date2);
    }

    public List<Order> getAllByCreateDateBetween(Date date1, Date date2) {
        return orderDao.getAllByIsDeletedFalseAndCreateDateBetween(date1, date2);
    }
}
