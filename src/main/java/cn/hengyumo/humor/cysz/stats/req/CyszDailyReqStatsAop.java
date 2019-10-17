package cn.hengyumo.humor.cysz.stats.req;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * ClickStatsAop
 * 通过aop 拦截cysz包下的控制器，实现点击量统计
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/10/13
 */
@Slf4j
@Aspect
@Component
public class CyszDailyReqStatsAop {

    @Pointcut(value = "(execution(* cn.hengyumo.humor.cysz.user.CyszUserController.*(..))" +
            "|| execution(* cn.hengyumo.humor.cysz.food.FoodController.*(..))" +
            "|| execution(* cn.hengyumo.humor.cysz.order.OrderController.*(..))" +
            "|| execution(* cn.hengyumo.humor.cysz.order.item.OrderItemController.*(..)))")
    private void req(){}

    @Resource
    private CyszDailyReqService cyszDailyReqService;

    @Before("req()")
    public void stats(){

        cyszDailyReqService.addOneTodayReq();
        log.info("访问量 + 1");
    }
}
