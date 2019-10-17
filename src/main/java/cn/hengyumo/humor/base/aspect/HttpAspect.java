package cn.hengyumo.humor.base.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 使用AOP对Http响应流程统一处理
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/4/17
 */
@Aspect
@Component
public class HttpAspect {

    private final static Logger logger = LoggerFactory.getLogger(HttpAspect.class);

    @Pointcut("execution(public * cn.hengyumo.www.base_spring_boot_jpa.b2c.controller.*.*(..))")
    public void log() {
    }

    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {
        logger.info("Before");

        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

        logger.info("url={}", request.getRequestURL());

        logger.info("method={}", request.getMethod());

        logger.info("ip={}", request.getRemoteAddr());

        logger.info("class_method={}", joinPoint.getSignature().getDeclaringTypeName() + "." +
                joinPoint.getSignature().getName());

        logger.info("args={}", joinPoint.getArgs());

    }

    @After("log()")
    public void doAfter() {
        System.out.println("After");
        logger.info("After");
    }

    @AfterReturning(returning = "object", pointcut = "log()")
    public void doAfterReturning(Object object) {
        if (object != null) {
            logger.info("response={}", object.toString());
        }
    }
}
