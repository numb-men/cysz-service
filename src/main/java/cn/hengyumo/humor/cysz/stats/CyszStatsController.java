package cn.hengyumo.humor.cysz.stats;

import cn.hengyumo.humor.base.annotation.SystemUserAuth;
import cn.hengyumo.humor.base.result.Result;
import cn.hengyumo.humor.base.system.config.SystemConfig;
import cn.hengyumo.humor.base.system.config.SystemResource;
import cn.hengyumo.humor.base.system.config.SystemResourceClass;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * CyszStatsController
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/10/13
 */
@Validated
@RestController
@RequestMapping("/cysz/stats")
@SystemResourceClass(resourceName = "cyszStats", comment = "串意十足统计",
        parentResource = SystemConfig.DEFAULT_RESOURCE_PARENT_CODE)
public class CyszStatsController {

    @Resource
    private CyszStatsService cyszStatsService;

    @SystemUserAuth
    @GetMapping("/view")
    @SystemResource(comment = "获取今日的数据视图")
    public Result view() {
        return Result.success(cyszStatsService.view());
    }

    @SystemUserAuth
    @GetMapping("/orderPie")
    @SystemResource(comment = "获取订单中食物售卖饼图")
    public Result orderPie() {
        return Result.success(cyszStatsService.orderPie());
    }

    @SystemUserAuth
    @GetMapping("/weekReqBar")
    @SystemResource(comment = "获取周用户活跃量条形图")
    public Result weekReqBar() {
        return Result.success(cyszStatsService.getWeekReqBar());
    }

    @SystemUserAuth
    @GetMapping("/foodWeekStats")
    @SystemResource(comment = "获取周食物销售情况")
    public Result foodWeekStats() {
        return Result.success(cyszStatsService.getFoodWeekStats());
    }
}
