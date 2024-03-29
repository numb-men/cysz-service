package cn.hengyumo.humor.system.controller;

import cn.hengyumo.humor.base.mvc.BaseController;
import cn.hengyumo.humor.system.annotation.SystemResourceClass;
import cn.hengyumo.humor.system.entity.SystemDictItemEntity;
import cn.hengyumo.humor.system.service.SystemDictItemService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * SystemDictItemController
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/18
 */
@RestController
@RequestMapping("/system/dict/item")
@SystemResourceClass(resourceName = "systemDictItem", comment = "系统字典项管理", parentResource = "system.dict")
public class SystemDictItemController extends BaseController<SystemDictItemService, SystemDictItemEntity, Long> {
}
