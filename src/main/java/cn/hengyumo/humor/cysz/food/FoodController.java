package cn.hengyumo.humor.cysz.food;

import cn.hengyumo.humor.base.mvc.BaseController;
import cn.hengyumo.humor.base.result.Result;
import cn.hengyumo.humor.base.system.config.SystemConfig;
import cn.hengyumo.humor.base.system.config.SystemResource;
import cn.hengyumo.humor.base.system.config.SystemResourceClass;
import cn.hengyumo.humor.cysz.base.CyszUserAuth;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * FoodController
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
