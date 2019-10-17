package cn.hengyumo.humor.cysz.order.item;

import cn.hengyumo.humor.base.mvc.BaseService;
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

    public List<OrderItem> getAllCreateDateBetween(Date date1, Date date2) {
        return orderItemDao.getAllByIsDeletedFalseAndCreateDateBetween(date1, date2);
    }
}
