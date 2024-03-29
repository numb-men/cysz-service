package cn.hengyumo.humor.cysz.controller;

import cn.hengyumo.humor.base.mvc.BaseController;
import cn.hengyumo.humor.base.result.Result;
import cn.hengyumo.humor.cysz.base.CyszUserAuth;
import cn.hengyumo.humor.cysz.entity.Food;
import cn.hengyumo.humor.cysz.service.FoodService;
import cn.hengyumo.humor.system.annotation.SystemResource;
import cn.hengyumo.humor.system.annotation.SystemResourceClass;
import cn.hengyumo.humor.system.config.SystemConfig;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * FoodController
 * 串意十足菜品 接口
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/10/12
 */
@Validated
@RestController
@RequestMapping("/cysz/food")
@SystemResourceClass(resourceName = "food", comment = "菜品",
        parentResource = SystemConfig.DEFAULT_RESOURCE_PARENT_CODE)
public class FoodController extends BaseController<FoodService, Food, Long> {

    @CyszUserAuth
    @GetMapping("/u/page")
    @SystemResource(comment = "前端用户获取菜品分页")
    public Result findAllPagedU(@RequestParam int pageNumber, @RequestParam int pageSize) {
        return Result.success(baseService.findAll(pageNumber, pageSize));
    }
}
