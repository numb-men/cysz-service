package cn.hengyumo.humor.system.controller;


import cn.hengyumo.humor.base.annotation.PassAuth;
import cn.hengyumo.humor.base.annotation.SystemUserAuth;
import cn.hengyumo.humor.base.mvc.BaseController;
import cn.hengyumo.humor.base.result.Result;
import cn.hengyumo.humor.system.annotation.SystemResource;
import cn.hengyumo.humor.system.annotation.SystemResourceClass;
import cn.hengyumo.humor.system.dto.SystemUserLoginDto;
import cn.hengyumo.humor.system.entity.SystemUser;
import cn.hengyumo.humor.system.service.SystemUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * SystemUserController
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/10
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/system/user")
@SystemResourceClass(resourceName = "systemUser", comment = "系统用户管理", parentResource = "system")
public class SystemUserController extends BaseController<SystemUserService, SystemUser, Long> {

    @PassAuth
    @PostMapping("/login")
    @SystemResource(comment = "系统用户登录")
    public Result login(@Valid @RequestBody SystemUserLoginDto systemUserLoginDto) {
        return Result.success(baseService.login(systemUserLoginDto));
    }

    @SystemUserAuth
    @PostMapping("/role")
    @SystemResource(comment = "给用户添加一个系统角色")
    public Result addOneRole(@RequestParam Long userId, @RequestParam Long roleId) {
        baseService.addRole(userId, roleId);
        return Result.success();
    }

    @SystemUserAuth
    @DeleteMapping("/role")
    @SystemResource(comment = "给用户删除一个系统角色")
    public Result removeOneRole(@RequestParam Long userId, @RequestParam Long roleId) {
        baseService.deleteRole(userId, roleId);
        return Result.success();
    }

    @SystemUserAuth
    @GetMapping("/roles")
    @SystemResource(comment = "获取对应id的用户的系统角色id列表")
    public Result roles(@RequestParam("id") Long id) {
        return Result.success(baseService.roles(id));
    }

    @SystemUserAuth
    @GetMapping("/password/reset")
    @SystemResource(comment = "重置对应id的用户的密码为初始密码")
    public Result resetPassword(@RequestParam("id") Long id) {
        baseService.resetPassword(id);
        return Result.success();
    }
}
