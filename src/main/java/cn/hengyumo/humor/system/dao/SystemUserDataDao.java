package cn.hengyumo.humor.system.dao;


import cn.hengyumo.humor.base.mvc.BaseDao;
import cn.hengyumo.humor.system.entity.SystemUserData;
import org.springframework.stereotype.Repository;

/**
 * SystemUserDataDao
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/9
 */
@Repository
public interface SystemUserDataDao extends BaseDao<SystemUserData, Long> {
}
