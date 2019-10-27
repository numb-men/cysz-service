package cn.hengyumo.humor.cysz.aspect;

import cn.hengyumo.humor.cysz.service.CyszDailyReqService;
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
public class CyszDailyReqStatsAspect {

    @Pointcut(value = "execution(* cn.hengyumo.humor.cysz.controller.*.*(..))")
    private void req(){}

    @Resource
    private CyszDailyReqService cyszDailyReqService;

    @Before("req()")
    public void stats(){

        cyszDailyReqService.addOneTodayReq();
        log.info("访问量 + 1");
    }
}
