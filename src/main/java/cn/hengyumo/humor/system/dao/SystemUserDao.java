package cn.hengyumo.humor.system.dao;


import cn.hengyumo.humor.base.mvc.BaseDao;
import cn.hengyumo.humor.system.entity.SystemUser;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;

/**
 * SystemUserDao
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/9
 */
@Repository
public interface SystemUserDao extends BaseDao<SystemUser, Long> {

    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
    SystemUser findByName(String name);
}
