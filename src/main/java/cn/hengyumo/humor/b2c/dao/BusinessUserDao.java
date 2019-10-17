package cn.hengyumo.humor.b2c.dao;

import cn.hengyumo.humor.b2c.entity.BusinessUser;
import cn.hengyumo.humor.base.mvc.BaseDao;
import org.springframework.stereotype.Repository;

/**
 * BusinessUserDao
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/16
 */
@Repository
public interface BusinessUserDao extends BaseDao<BusinessUser, Long> {
}
