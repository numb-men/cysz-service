package cn.hengyumo.humor.cysz.service;

import cn.hengyumo.humor.base.mvc.BaseService;
import cn.hengyumo.humor.cysz.dao.OrderItemDao;
import cn.hengyumo.humor.cysz.entity.OrderItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * OrderItemService
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/10/12
 */
@Slf4j
@Service
public class OrderItemService extends BaseService<OrderItem, Long> {

    @Resource
    private OrderItemDao orderItemDao;

    /**
     * 获取一段时间内新增的订单项
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 订单项列表
     */
    public List<OrderItem> getAllCreateDateBetween(Date date1, Date date2) {
        return orderItemDao.getAllByIsDeletedFalseAndCreateDateBetween(date1, date2);
    }
}
