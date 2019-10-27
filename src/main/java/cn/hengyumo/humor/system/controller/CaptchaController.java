package cn.hengyumo.humor.system.controller;

import cn.hengyumo.humor.base.annotation.PassAuth;
import cn.hengyumo.humor.base.result.Result;
import cn.hengyumo.humor.system.annotation.SystemResource;
import cn.hengyumo.humor.system.annotation.SystemResourceClass;
import cn.hengyumo.humor.system.service.CaptchaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * CaptchaController
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/20
 */

@RestController
@RequestMapping("/system/captcha")
@SystemResourceClass(resourceName = "systemCaptcha", comment = "验证码", parentResource = "system")
public class CaptchaController {

    @Resource
    private CaptchaService captchaService;

    @PassAuth
    @GetMapping("/create")
    @SystemResource(comment = "生成验证码")
    public Result create() {
        return Result.success(captchaService.createCaptcha());
    }
}
