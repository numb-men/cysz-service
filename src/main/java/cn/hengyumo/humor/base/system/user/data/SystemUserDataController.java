package cn.hengyumo.humor.base.system.user.data;

import cn.hengyumo.humor.base.annotation.SystemUserAuth;
import cn.hengyumo.humor.base.mvc.BaseController;
import cn.hengyumo.humor.base.result.Result;
import cn.hengyumo.humor.base.system.config.SystemResource;
import cn.hengyumo.humor.base.system.config.SystemResourceClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * SystemUserDataController
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/19
 */
@Slf4j
@RestController
@RequestMapping("/system/user/data")
@SystemResourceClass(resourceName = "systemUserData", comment = "系统用户资料管理", parentResource = "system")
public class SystemUserDataController extends BaseController<SystemUserDataService, SystemUserData, Long> {

    @SystemUserAuth
    @GetMapping("/detail/list")
    @SystemResource(comment = "全部用户的详细信息")
    public Result detailList() {
        return Result.success(baseService.detailList());
    }

    @SystemUserAuth
    @PostMapping("/detail")
    @SystemResource(comment = "新建并保存用户详细信息")
    public Result saveDetail(@Valid @RequestBody SystemUserDetailDto systemUserDetailDto) {
        return Result.success(baseService.save(systemUserDetailDto));
    }

    @SystemUserAuth
    @PutMapping("/detail")
    @SystemResource(comment = "更新并保存用户详细信息")
    public Result updateDetail(@RequestParam("id") Long id,
                         @Valid @RequestBody SystemUserDetailDto systemUserDetailDto) {
        return Result.success(baseService.update(id, systemUserDetailDto));
    }

    @SystemUserAuth
    @GetMapping("/detail")
    @SystemResource(comment = "由id获取用户的详细信息")
    public Result detail(@RequestParam("id") Long id) {
        return Result.success(baseService.detail(id));
    }

    @SystemUserAuth
    @PostMapping("/search")
    @SystemResource(comment = "搜索用户并分页")
    public Result searchAndPaged(@RequestParam int pageNumber, @RequestParam int pageSize,
                                 @RequestBody SystemUserDetailSearchDto searchDto) {
        return Result.success(baseService.searchAndPaged(pageNumber, pageSize, searchDto));
    }
}
