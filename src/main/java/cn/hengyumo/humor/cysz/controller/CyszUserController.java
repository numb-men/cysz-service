package cn.hengyumo.humor.cysz.controller;

import cn.hengyumo.humor.base.annotation.PassAuth;
import cn.hengyumo.humor.base.annotation.SystemUserAuth;
import cn.hengyumo.humor.base.mvc.BaseController;
import cn.hengyumo.humor.base.result.Result;
import cn.hengyumo.humor.cysz.base.CyszUserAuth;
import cn.hengyumo.humor.cysz.dto.CyszUserDetailDto;
import cn.hengyumo.humor.cysz.dto.CyszUserLoginDto;
import cn.hengyumo.humor.cysz.dto.CyszUserRegisterDto;
import cn.hengyumo.humor.cysz.dto.CyszUserSearchDto;
import cn.hengyumo.humor.cysz.entity.CyszUser;
import cn.hengyumo.humor.cysz.service.CyszUserService;
import cn.hengyumo.humor.system.annotation.SystemResource;
import cn.hengyumo.humor.system.annotation.SystemResourceClass;
import cn.hengyumo.humor.system.config.SystemConfig;
import cn.hengyumo.humor.utils.BaseUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Min;

/**
 * CyszUserController
 * 串意十足用户 接口
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/10/12
 */
@Validated
@RestController
@RequestMapping("/cysz/user")
@SystemResourceClass(resourceName = "cyszUser", comment = "串意十足用户",
        parentResource = SystemConfig.DEFAULT_RESOURCE_PARENT_CODE)
public class CyszUserController extends BaseController<CyszUserService, CyszUser, Long> {

    @Resource
    private BaseUtil baseUtil;

    @PassAuth
    @PostMapping("/login")
    @SystemResource(comment = "登录")
    public Result login(@Valid @RequestBody CyszUserLoginDto cyszUserLoginDto) {
        return Result.success(baseService.login(cyszUserLoginDto));
    }

    @PassAuth
    @PostMapping("/register")
    @SystemResource(comment = "注册")
    public Result register(@Valid @RequestBody CyszUserRegisterDto cyszUserRegisterDto) {
        return Result.success(baseService.register(cyszUserRegisterDto));
    }

    @SystemUserAuth
    @PostMapping("/search")
    @SystemResource(comment = "搜索用户并分页")
    public Result searchAndPaged(@RequestParam int pageNumber, @RequestParam int pageSize,
                                 @RequestBody CyszUserSearchDto searchDto) {
        return Result.success(baseService.searchAndPaged(pageNumber, pageSize, searchDto));
    }

    @SystemUserAuth
    @PostMapping("/detail")
    @SystemResource(comment = "新增用户详细信息")
    public Result saveDetail(@Valid @RequestBody CyszUserDetailDto cyszUserDetailDto) {
        return Result.success(baseService.saveOrUpdateDetail(cyszUserDetailDto));
    }

    @SystemUserAuth
    @PutMapping("/detail")
    @SystemResource(comment = "修改用户详细信息")
    public Result updateDetail(@Valid @RequestBody CyszUserDetailDto cyszUserDetailDto) {
        return Result.success(baseService.saveOrUpdateDetail(cyszUserDetailDto));
    }

    @CyszUserAuth
    @GetMapping("/self")
    @SystemResource(comment = "获取自己的信息")
    public Result getSelf() {
        return Result.success(baseUtil.getCyszCurrentUser());
    }

    @CyszUserAuth
    @GetMapping("/recharge")
    @SystemResource(comment = "充值")
    public Result recharge(@Min(value = 0) @RequestParam Double num) {
        return Result.success(baseService.recharge(num));
    }
}
