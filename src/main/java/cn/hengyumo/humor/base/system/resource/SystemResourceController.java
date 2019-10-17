package cn.hengyumo.humor.base.system.resource;


import cn.hengyumo.humor.base.annotation.SystemAdminAuth;
import cn.hengyumo.humor.base.mvc.BaseController;
import cn.hengyumo.humor.base.result.Result;
import cn.hengyumo.humor.base.system.config.SystemResource;
import cn.hengyumo.humor.base.system.config.SystemResourceClass;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * SystemResourceController
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/11
 */
@RestController
@RequestMapping("/system/resource")
@SystemResourceClass(resourceName = "systemResource", comment = "系统资源管理", parentResource = "system")
public class SystemResourceController extends BaseController<SystemResourceService, SystemResourceEntity, Long> {

    @SystemAdminAuth
    @SystemResource(name = "systemResourceDeleteByIdCascade", comment = "根据id级联删除资源",
            code = "system.resource.delete.by.id.cascade")
    @DeleteMapping("/cascade")
    public Result deleteByIdCascade(Long id) {
        baseService.deleteResourcesByIdCascade(id);
        return Result.success();
    }
}
