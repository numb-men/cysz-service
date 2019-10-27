package cn.hengyumo.humor.utils;


import cn.hengyumo.humor.base.exception.common.BaseException;
import cn.hengyumo.humor.base.exception.enums.BaseResultEnum;
import cn.hengyumo.humor.system.entity.SystemAdmin;
import cn.hengyumo.humor.utils.date.DateUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Date;

/**
 * TokenUtil
 * token工具
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/4/25
 */
public class TokenUtil {

    private final static String KEY = "a7J23(2&^ad,34NK2:q'23Ja0peq)&124=";

    /**
     * 从token中取出id
     *
     * @author hengyumo
     * @since 2019/4/25
     *
     * @param httpServletRequest http请求
     * @return id
     */
    public static Long getUserIdByRequest(HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("token");
        if (token == null) {
            token = (String) httpServletRequest.getAttribute("token");
        }
        return Long.valueOf(JWT.decode(token).getAudience().get(0));
    }

    /**
     * 从当前request的token中取出id
     *
     * @author hengyumo
     * @since 2019/4/25
     *
     * @return id
     */
    public static Long getUserId() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
        return getUserIdByRequest(request);
    }

    /**
     * 根据wx User信息生成token
     *
     * @author hengyumo
     * @since 2019/4/25
     *
     * @param id id
     * @param pwd 密码
     * @return token
     */
    public static <ID extends Serializable, PWD extends String> String createWxToken(ID id, PWD pwd) {
        // 存储id，2小时后过期，以 password 作为 token 的密钥
        return JWT.create()
                .withAudience(String.valueOf(id))
                .withExpiresAt(DateUtil.addHour(new Date(), 2))
                .sign(Algorithm.HMAC256(pwd));
    }

    /**
     * 根据User信息生成token
     *
     * @author hengyumo
     * @since 2019/4/25
     *
     * @param id id
     * @param pwd 密码
     * @return token
     */
    public static <ID extends Serializable, PWD extends String> String createToken(ID id, PWD pwd) {
        // 存储id，2小时后过期，以 password 作为 token 的密钥
        return JWT.create()
                .withAudience(String.valueOf(id))
                .withExpiresAt(DateUtil.addHour(new Date(), 2))
                .sign(Algorithm.HMAC256(pwd));
    }

    /**
     * 生成admin token
     *
     * @param name name
     * @param password password
     * @return token
     */
    public static String createAdminToken(String name, String password) {
        return JWT.create()
                .withAudience(name)
                .withExpiresAt(DateUtil.addHour(new Date(), 6))
                .sign(Algorithm.HMAC256(String.format("%s_%s", KEY, password)));
    }

    /**
     * 根据User id生成token
     *
     * @author hengyumo
     * @since 2019/4/25
     *
     * @param id id
     * @return token
     */
    public static <ID extends Serializable> String createToken(ID id) {
        return createToken(id, String.format("%s_%s", KEY, id));
    }

    /**
     * 根据密码验证token
     *
     * @param token token
     * @param pwd pwd
     *
     * @return boolean
     */
    public static boolean verifyToken(String token, String pwd){
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(pwd)).build();
        try {
            jwtVerifier.verify(token);
        }
        catch (JWTVerificationException e) {
            throw new BaseException(BaseResultEnum.TOKEN_CHECK_FAILED);
        }
        return true;
    }


    /**
     * 根据用户 id 验证token
     *
     * @param token token
     * @return boolean
     */
    public static boolean verifyToken(String token){
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(
                String.format("%s_%s", KEY, getUserId()))).build();
        try {
            jwtVerifier.verify(token);
        }
        catch (JWTVerificationException e) {
            throw new BaseException(BaseResultEnum.TOKEN_CHECK_FAILED);
        }
        return true;
    }

    /**
     * 验证admin，不抛出异常
     *
     * @param systemAdmin properties
     * @param token token
     * @return boolean
     */
    public static boolean tryCheckAdmin(SystemAdmin systemAdmin, String token) {
        if (token == null) {
            return false;
        }
        String name;
        try {
            name = String.valueOf(JWT.decode(token).getAudience().get(0));
        }
        catch (JWTDecodeException j) {
            return false;
        }
        if (! name.equals(systemAdmin.getName())) {
            return false;
        }
        else {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(
                    String.format("%s_%s", KEY, systemAdmin.getPassword()))).build();
            try {
                jwtVerifier.verify(token);
            }
            catch (JWTVerificationException e) {
                return false;
            }
            return true;
        }
    }

    /**
     * 验证admin
     *
     * @param systemAdmin properties
     * @param token token
     * @return boolean
     */
    public static boolean checkAdmin(SystemAdmin systemAdmin, String token) {
        if (token == null) {
            throw new BaseException(BaseResultEnum.NOT_LOGIN);
        }
        String name;
        try {
            name = String.valueOf(JWT.decode(token).getAudience().get(0));
        }
        catch (JWTDecodeException j) {
            throw new BaseException(BaseResultEnum.TOKEN_CHECK_FAILED);
        }
        if (! name.equals(systemAdmin.getName())) {
            throw new BaseException(BaseResultEnum.NOT_POWER);
        }
        else {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(
                    String.format("%s_%s", KEY, systemAdmin.getPassword()))).build();
            try {
                jwtVerifier.verify(token);
            }
            catch (JWTVerificationException e) {
                throw new BaseException(BaseResultEnum.NOT_POWER);
            }
            return true;
        }
    }
}
