package cn.hengyumo.humor.base.system.resource;


import cn.hengyumo.humor.base.mvc.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * SystemResourceDao
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/10
 */
@Repository
public interface SystemResourceDao extends BaseDao<SystemResourceEntity, Long> {

    Boolean existsByCode(String code);

    List<SystemResourceEntity> findAllByParentId(Long parentId);
}
