package cn.hengyumo.humor.cysz.order.item;

import cn.hengyumo.humor.base.mvc.BaseDao;
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

    List<OrderItem> getAllByIsDeletedFalseAndCreateDateBetween(Date date1, Date date2);
}
