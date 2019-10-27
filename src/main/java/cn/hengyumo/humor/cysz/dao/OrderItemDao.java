package cn.hengyumo.humor.cysz.dao;

import cn.hengyumo.humor.base.mvc.BaseDao;
import cn.hengyumo.humor.cysz.entity.OrderItem;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * OrderItemDao
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/10/12
 */
@Repository
public interface OrderItemDao extends BaseDao<OrderItem, Long> {

    /**
     * 获取一段时间内新增的订单项
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 订单项列表
     */
    List<OrderItem> getAllByIsDeletedFalseAndCreateDateBetween(Date date1, Date date2);
}
