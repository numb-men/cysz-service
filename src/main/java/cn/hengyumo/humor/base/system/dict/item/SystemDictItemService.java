package cn.hengyumo.humor.base.system.dict.item;

import cn.hengyumo.humor.base.mvc.BaseService;
import org.springframework.stereotype.Service;

/**
 * SystemDictItemService
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/18
 */
@Service
public class SystemDictItemService extends BaseService<SystemDictItemEntity, Long> {

    public void clear() {
        baseDao.deleteAll();
    }
}
