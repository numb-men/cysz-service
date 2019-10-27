package cn.hengyumo.humor.cysz.service;

import cn.hengyumo.humor.base.exception.common.BaseException;
import cn.hengyumo.humor.base.exception.enums.BaseResultEnum;
import cn.hengyumo.humor.base.mvc.BaseService;
import cn.hengyumo.humor.cysz.dao.OrderDao;
import cn.hengyumo.humor.cysz.dto.OrderItemDto;
import cn.hengyumo.humor.cysz.dto.OrderSearchDto;
import cn.hengyumo.humor.cysz.entity.*;
import cn.hengyumo.humor.cysz.enums.OrderStatus;
import cn.hengyumo.humor.cysz.exception.common.FoodException;
import cn.hengyumo.humor.cysz.exception.common.OrderException;
import cn.hengyumo.humor.cysz.exception.enums.FoodResultEnum;
import cn.hengyumo.humor.cysz.exception.enums.OrderResultEnum;
import cn.hengyumo.humor.cysz.vo.OrderSearchVo;
import cn.hengyumo.humor.utils.BaseUtil;
import com.github.wenhao.jpa.PredicateBuilder;
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


    /**
     * 创建订单
     *
     * @param orderItemDtos 订单项表单列表
     * @return 创建的订单
     */
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

    /**
     * 订单查找和分页
     *
     * @param pageNumber 页数
     * @param pageSize 页大小
     * @param searchDto 查找表单
     * @return 订单分页
     */
    public Page<OrderSearchVo> systemSearchPaged(Integer pageNumber, Integer pageSize,
                                                 OrderSearchDto searchDto) {
        Page<Order> page;
        Page<OrderSearchVo> resultPage;
        Specification<Order> specification = pageSize > 0 ?
                createSearch(searchDto) : createSearch(new OrderSearchDto());
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        page = findAll(specification, pageable);
        List<OrderSearchVo> orderSearchVos = new ArrayList<>();
        List<Order> orders = page.getContent();
        for (Order order : orders) {
            OrderSearchVo orderSearchVo = new OrderSearchVo();
            orderSearchVo.setId(order.getId());
            orderSearchVo.setAllPrice(order.getAllPrice());
            orderSearchVo.setCreateDate(order.getCreateDate());
            orderSearchVo.setMobile(order.getCyszUser().getMobile());
            orderSearchVo.setStatus(order.getStatus());
            orderSearchVo.setUsername(order.getCyszUser().getUsername());
            Integer foodNum = 0;
            StringBuilder stringBuilder = new StringBuilder();
            for (OrderItem orderItem : order.getOrderItems()) {
                foodNum += orderItem.getNum();
                stringBuilder.append(orderItem.getFood().getTitle());
                stringBuilder.append(String.format("x%d、", orderItem.getNum()));
            }
            orderSearchVo.setFoodNum(foodNum);
            String content = stringBuilder.toString();
            if (content.length() > 40) {
                content = content.substring(0, 40) + "...";
            }
            orderSearchVo.setContent(content);
            orderSearchVo.setOrderDetail(order);
            orderSearchVos.add(orderSearchVo);
        }
        resultPage = new PageImpl<>(orderSearchVos, pageable, page.getTotalElements());

        return resultPage;
    }

    /**
     * 格式化like语句
     *
     * @param s 参数
     * @return 格式化后的like语句
     */
    private String likeFormat(String s) {
        return String.format("%%%s%%", s);
    }

    /**
     * 判断字符串非空
     *
     * @param s 字符串
     * @return 字符串是否为空
     */
    private boolean notBlank(String s) {
        return s != null && ! s.equals("");
    }

    /**
     * 根据查询表单生成动态查询
     *
     * @param searchDto 查询表单
     * @return 动态查询
     */
    private Specification<Order> createSearch(OrderSearchDto searchDto) {
        PredicateBuilder<Order> p1 = Specifications.<Order>and()
                .eq("isDeleted", false);
        PredicateBuilder<CyszUser> p2 = Specifications.or();
        boolean needSpec = false;
        // 用户名为空时，不依据用户名查询
        if (notBlank(searchDto.getUsername())) {
            p2 = p2.like("cyszUser.username", likeFormat(searchDto.getUsername()));
            needSpec = true;
        }
        // 手机号为空时，不依据手机号查询
        if (notBlank(searchDto.getMobile())) {
            p2 = p2.like("cyszUser.mobile", likeFormat(searchDto.getMobile()));
            needSpec = true;
        }
        // 状态为空或者状态码为负数时，不依据状态来查询
        if (searchDto.getStatus() != null && searchDto.getStatus() > 0) {
            p2 = p2.eq("status", searchDto.getStatus());
            needSpec = true;
        }
        // 之前三个条件至少有一个满足时生成动态查询语句
        if (needSpec) {
            return p1.predicate(p2.build()).build();
        }
        return p1.build();
    }

    /**
     * 确认订单已送达
     *
     * @param id 订单id
     * @return 订单
     */
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
            if (!order.getCyszUser().getId().equals(cyszCurrentUser.getId())) {
                throw new OrderException(OrderResultEnum.NOT_YOU_ORDER);
            }
        }
        order.setStatus(OrderStatus.HAS_ARRIVED.getCode());
        return save(order);
    }

    /**
     * 订单退款
     *
     * @param id 订单退款
     * @return 订单
     */
    @Transactional
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

    /**
     * 用户查找自己的订单并分页
     *
     * @param pageNumber 页号
     * @param pageSize 页大小
     * @return 订单分页
     */
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
        } else if (pageSize == 0) {
            page = new PageImpl<>(findAll());
        } else {
            page = new PageImpl<>(findAll(specification));
        }
        return page;
    }
    /**
     * 获取在一段时间内新增的订单数
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 新增订单数
     */
    public Long countByCreateDateBetween(Date date1, Date date2) {
        return orderDao.countByCreateDateBetween(date1, date2);
    }

    /**
     * 获取一段时间内的所有订单
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 订单列表
     */
    public List<Order> getAllByCreateDateBetween(Date date1, Date date2) {
        return orderDao.getAllByIsDeletedFalseAndCreateDateBetween(date1, date2);
    }
}
