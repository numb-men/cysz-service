package cn.hengyumo.humor.wx.user;

import cn.hengyumo.humor.base.annotation.PassAuth;
import cn.hengyumo.humor.base.mvc.BaseController;
import cn.hengyumo.humor.base.result.Result;
import cn.hengyumo.humor.base.system.config.SystemConfig;
import cn.hengyumo.humor.base.system.config.SystemResource;
import cn.hengyumo.humor.base.system.config.SystemResourceClass;
import cn.hengyumo.humor.wx.base.WxUserAuth;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * WxUserController
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/14
 */
@RestController
@RequestMapping("/wx/user")
@SystemResourceClass(resourceName = "wxUser", comment = "微信用户管理",
        parentResource = SystemConfig.DEFAULT_RESOURCE_PARENT_CODE)
public class WxUserController extends BaseController<WxUserService, WxUser, Long> {

    @PassAuth
    @GetMapping("/login")
    @SystemResource(comment = "微信小程序登录")
    public Result login(@RequestParam String code) {
        return Result.success(baseService.login(code));
    }

    @WxUserAuth
    @PostMapping("/info")
    @SystemResource(comment = "保存或更新微信用户信息")
    public Result saveInfo(WxUserInfoDto wxUserInfoDto) {
        baseService.saveInfo(wxUserInfoDto);
        return Result.success();
    }
}
