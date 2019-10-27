package cn.hengyumo.humor.b2c.controller;

import cn.hengyumo.humor.b2c.entity.BusinessUser;
import cn.hengyumo.humor.b2c.service.BusinessUserService;
import cn.hengyumo.humor.base.mvc.BaseController;
import cn.hengyumo.humor.system.annotation.SystemResourceClass;
import cn.hengyumo.humor.system.config.SystemConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * BusinessUserController
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/16
 */

@Slf4j
@Validated
@RestController
@RequestMapping("/b2c/business/user")
@SystemResourceClass(resourceName = "businessUser", comment = "商户",
        parentResource = SystemConfig.DEFAULT_RESOURCE_PARENT_CODE)
public class BusinessUserController extends BaseController<BusinessUserService, BusinessUser, Long> {
}
