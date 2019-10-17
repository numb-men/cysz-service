package cn.hengyumo.humor.base.system.dict;

import cn.hengyumo.humor.base.mvc.BaseDao;
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
