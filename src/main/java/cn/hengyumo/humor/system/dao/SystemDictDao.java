package cn.hengyumo.humor.system.dao;

import cn.hengyumo.humor.base.mvc.BaseDao;
import cn.hengyumo.humor.system.entity.SystemDictEntity;
import org.springframework.stereotype.Repository;

/**
 * SystemDictDao
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/18
 */
@Repository
public interface SystemDictDao extends BaseDao<SystemDictEntity, Long> {

    SystemDictEntity findByName(String name);

    void deleteByName(String name);
}
