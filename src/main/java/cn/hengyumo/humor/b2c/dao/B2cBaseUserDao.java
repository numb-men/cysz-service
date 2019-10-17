package cn.hengyumo.humor.b2c.dao;

import cn.hengyumo.humor.b2c.entity.B2cBaseUser;
import cn.hengyumo.humor.base.mvc.BaseDao;
import org.springframework.stereotype.Repository;

/**
 * B2cBaseUserDao
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/16
 */
@Repository
public interface B2cBaseUserDao extends BaseDao<B2cBaseUser, Long> {
}
