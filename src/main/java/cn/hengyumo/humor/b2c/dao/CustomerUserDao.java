package cn.hengyumo.humor.b2c.dao;

import cn.hengyumo.humor.b2c.entity.CustomerUser;
import cn.hengyumo.humor.base.mvc.BaseDao;
import org.springframework.stereotype.Repository;

/**
 * CustomerUserDao
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/16
 */
@Repository
public interface CustomerUserDao extends BaseDao<CustomerUser, Long> {
}
