package cn.hengyumo.humor.base.system.role;


import cn.hengyumo.humor.base.annotation.SystemUserAuth;
import cn.hengyumo.humor.base.mvc.BaseController;
import cn.hengyumo.humor.base.result.Result;
import cn.hengyumo.humor.base.system.config.SystemResource;
import cn.hengyumo.humor.base.system.config.SystemResourceClass;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * SystemRoleController
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/11
 */
@RestController
@RequestMapping("/system/role")
@SystemResourceClass(resourceName = "systemRole", comment = "系统角色管理", parentResource = "system")
public class SystemRoleController extends BaseController<SystemRoleService, SystemRoleEntity, Long> {

    @SystemUserAuth
    @PostMapping("/create")
    @SystemResource(comment = "新建角色并添加资源")
    public Result create(@Valid @RequestBody SystemRoleDto systemRoleDto) {
        return Result.success(baseService.create(systemRoleDto));
    }

    @SystemUserAuth
    @PutMapping("/update")
    @SystemResource(comment = "更新角色并修改资源")
    public Result update(@RequestParam("id") Long id, @Valid @RequestBody SystemRoleDto systemRoleDto) {
        return Result.success(baseService.update(id, systemRoleDto));
    }

    @SystemUserAuth
    @GetMapping("/view")
    @SystemResource(comment = "查看角色详情")
    public Result view(@RequestParam Long id) {
        return Result.success(baseService.view(id));
    }

    @SystemUserAuth
    @PostMapping("/resource")
    @SystemResource(comment = "给角色添加一个系统资源")
    public Result addOneResource(@RequestParam Long roleId, @RequestParam Long resourceId) {
        baseService.addResource(roleId, resourceId);
        return Result.success();
    }

    @SystemUserAuth
    @DeleteMapping("/resource")
    @SystemResource(comment = "给角色删除一个系统资源")
    public Result removeOneResource(@RequestParam Long roleId, @RequestParam Long resourceId) {
        baseService.deleteResource(roleId, resourceId);
        return Result.success();
    }
}
