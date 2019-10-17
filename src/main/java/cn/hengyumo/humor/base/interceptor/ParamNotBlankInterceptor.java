package cn.hengyumo.humor.base.interceptor;


import cn.hengyumo.humor.base.annotation.ParamNotBlank;
import cn.hengyumo.humor.base.exception.common.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * ParamNotBlankInterceptor
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/8/7
 */
public class ParamNotBlankInterceptor implements HandlerInterceptor {

    private final static Logger logger = LoggerFactory.getLogger(ParamNotBlankInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod)handler;
        Method method = handlerMethod.getMethod();

        if (method.isAnnotationPresent(ParamNotBlank.class)) {
            ParamNotBlank paramNotBlank = method.getAnnotation(ParamNotBlank.class);
            String[] params = paramNotBlank.values();
            // get param from requestParam get/post
            StringBuilder errMsg = new StringBuilder();
            for (String param : params) {
                String value = request.getParameter(param);
                if ("".equals(value) || value == null) {
                    // "" or null
                    errMsg.append(String.format("%s is blank!\n", param));
                }
            }
            logger.info(errMsg.toString());
            if (errMsg.length() > 0) {
                throw new BaseException(errMsg.toString());
            }
        }
        return true;
    }
}
