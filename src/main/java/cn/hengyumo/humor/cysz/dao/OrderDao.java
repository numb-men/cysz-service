package cn.hengyumo.humor.cysz.dao;

import cn.hengyumo.humor.base.mvc.BaseDao;
import cn.hengyumo.humor.cysz.entity.Order;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * OrderDao
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/10/12
 */
@Repository
public interface OrderDao extends BaseDao<Order, Long> {

    /**
     * 获取在一段时间内新增的订单数
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 新增订单数
     */
    Long countByCreateDateBetween(Date date1, Date date2);

    /**
     * 获取一段时间内的所有订单
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 订单列表
     */
    List<Order> getAllByIsDeletedFalseAndCreateDateBetween(Date date1, Date date2);
}
