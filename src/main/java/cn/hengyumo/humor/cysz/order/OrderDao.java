package cn.hengyumo.humor.cysz.order;

import cn.hengyumo.humor.base.mvc.BaseDao;
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

    Long countByCreateDateBetween(Date date1, Date date2);

    List<Order> getAllByIsDeletedFalseAndCreateDateBetween(Date date1, Date date2);
}
