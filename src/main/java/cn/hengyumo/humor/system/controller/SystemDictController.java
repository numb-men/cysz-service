package cn.hengyumo.humor.system.controller;

import cn.hengyumo.humor.base.annotation.SystemAdminAuth;
import cn.hengyumo.humor.base.mvc.BaseController;
import cn.hengyumo.humor.base.result.Result;
import cn.hengyumo.humor.system.annotation.SystemResource;
import cn.hengyumo.humor.system.annotation.SystemResourceClass;
import cn.hengyumo.humor.system.entity.SystemDictEntity;
import cn.hengyumo.humor.system.entity.SystemDictItemEntity;
import cn.hengyumo.humor.system.service.SystemDictService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * SystemDictController
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/18
 */
@RestController
@RequestMapping("/system/dict")
@SystemResourceClass(resourceName = "systemDict", comment = "系统字典管理", parentResource = "system")
public class SystemDictController extends BaseController<SystemDictService, SystemDictEntity, Long> {

    @SystemAdminAuth
    @SystemResource(name = "systemDictFindByName", comment = "由字典名查询字典", code = "system.dict.find.by.name")
    @RequestMapping("/name/is")
    public Result findByName(@RequestParam String name) {
        return Result.success(baseService.findByName(name));
    }

    @SystemAdminAuth
    @SystemResource(name = "systemDictDeleteByIdCascade", comment = "根据id级联删除字典",
            code = "system.dict.delete.by.id.cascade")
    @DeleteMapping("/cascade")
    public Result deleteByIdCascade(Long id) {
        baseService.deleteDictByIdCascade(id);
        return Result.success();
    }

    @SystemAdminAuth
    @SystemResource(name = "systemDictSaveItem", comment = "给对应id的字典增加或更新字典项",
            code = "system.dict.save.item")
    @PostMapping("/save/item")
    public Result saveDictItem(@RequestParam Long id,
                              @RequestBody @Valid SystemDictItemEntity systemDictItemEntity) {
        return Result.success(baseService.addOrUpdateDictItem(id, systemDictItemEntity));
    }
}
