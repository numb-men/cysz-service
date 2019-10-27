package cn.hengyumo.humor.system.controller;


import cn.hengyumo.humor.base.annotation.SystemAdminAuth;
import cn.hengyumo.humor.base.result.Result;
import cn.hengyumo.humor.system.annotation.SystemResource;
import cn.hengyumo.humor.system.annotation.SystemResourceClass;
import cn.hengyumo.humor.system.config.SystemConfig;
import cn.hengyumo.humor.system.service.SystemService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * SystemController
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/10
 */
@RestController
@RequestMapping("/system")
@SystemResourceClass(resourceName = "system", comment = "系统管理",
        parentResource = SystemConfig.DEFAULT_RESOURCE_PARENT_CODE)
public class SystemController {

    @Resource
    private SystemService systemService;


    @SystemAdminAuth
    @GetMapping("/resource/create")
    @SystemResource(name = "systemResourceCreate", comment = "生成系统资源", code = "system.resource.create")
    public Result createResources(HttpServletRequest request) {
        systemService.createResources(request);
        return Result.success();
    }

    @SystemAdminAuth
    @DeleteMapping("/resource/clear")
    @SystemResource(name = "systemResourceClear", comment = "清空系统资源", code = "system.resource.clear")
    public Result clearResources() {
        systemService.clearResources();
        return Result.success();
    }

    @SystemAdminAuth
    @GetMapping("/dict/create")
    @SystemResource(name = "systemDictCreate", comment = "生成系统字典", code = "system.dict.create")
    public Result createDict() {
        systemService.createDict();
        return Result.success();
    }

    @SystemAdminAuth
    @DeleteMapping("/dict/clear")
    @SystemResource(name = "systemDictClear", comment = "清空系统字典", code = "system.dict.clear")
    public Result clearDict() {
        systemService.clearDict();
        return Result.success();
    }
}
