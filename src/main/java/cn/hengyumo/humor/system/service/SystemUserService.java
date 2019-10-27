package cn.hengyumo.humor.system.service;


import cn.hengyumo.humor.base.cache.EhcacheCache;
import cn.hengyumo.humor.base.exception.common.BaseException;
import cn.hengyumo.humor.base.exception.enums.BaseResultEnum;
import cn.hengyumo.humor.base.mvc.BaseEntity;
import cn.hengyumo.humor.base.mvc.BaseService;
import cn.hengyumo.humor.system.dao.SystemRoleDao;
import cn.hengyumo.humor.system.dao.SystemUserDao;
import cn.hengyumo.humor.system.dto.SystemUserLoginDto;
import cn.hengyumo.humor.system.entity.SystemAdmin;
import cn.hengyumo.humor.system.entity.SystemRoleEntity;
import cn.hengyumo.humor.system.entity.SystemUser;
import cn.hengyumo.humor.system.exception.common.SystemException;
import cn.hengyumo.humor.system.exception.enums.SystemResultEnum;
import cn.hengyumo.humor.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SystemUserService
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/10
 */
@Service
public class SystemUserService extends BaseService<SystemUser, Long> {

    @Resource
    private SystemUserDao systemUserDao;

    @Resource
    private SystemRoleDao systemRoleDao;

    @Resource
    private SystemAdmin systemAdmin;

    @Value("${hengyumo.base.system.default.password}")
    private String defaultPassword;

    private SystemUser systemUser;
    private SystemRoleEntity systemRoleEntity;


    public String login(SystemUserLoginDto systemUserLoginDto) {
        // 验证码
        String captchaText = (String) EhcacheCache.getValue(
                "captchaCache", systemUserLoginDto.getCaptchaToken());
        if (captchaText == null) {
            throw new BaseException(BaseResultEnum.CAPTCHA_EXPIRE);
        }
        if (! captchaText.toLowerCase().equals(systemUserLoginDto.getCode().toLowerCase())) {
            throw new BaseException(BaseResultEnum.CAPTCHA_ERROR);
        }
        // 超级管理员
        String token = adminLogin(systemUserLoginDto);
        if (token != null) {
            return token;
        }
        // 用户密码校验
        SystemUser user = systemUserDao.findByName(systemUserLoginDto.getName());
        if (user == null) {
            throw new BaseException(BaseResultEnum.NOT_USER);
        }
        if (! user.getPassword().equals(systemUserLoginDto.getPassword())) {
            throw new BaseException(BaseResultEnum.PASSWORD_ERROR);
        }
        user.getUserData().setLastLogin(new Date());
        save(user);
        return TokenUtil.createToken(user.getId(), user.getPassword());
    }

    public void resetPassword(Long id) {
        systemUser = findOne(id).orElse(null);
        if (systemUser == null) {
            throw new SystemException(SystemResultEnum.USER_NOT_FOUND);
        }
        systemUser.setPassword(defaultPassword);
        save(systemUser);
    }

    public SystemUser findOneUserNotNull(Long id) {
        SystemUser user = findOne(id).orElse(null);
        if (user == null) {
            throw new BaseException(BaseResultEnum.USER_NOT_FIND);
        }
        return user;
    }

    private String adminLogin(SystemUserLoginDto systemUserLoginDto) {
        if (systemUserLoginDto.getName().equals(systemAdmin.getName()) &&
            systemUserLoginDto.getPassword().equals(systemAdmin.getPassword())) {
            return TokenUtil.createAdminToken(systemUserLoginDto.getName(), systemUserLoginDto.getPassword());
        }
        return null;
    }

    public List<Long> roles(Long id) {
        systemUser = findOne(id).orElse(null);
        if (systemUser == null) {
            throw new SystemException(SystemResultEnum.USER_NOT_FOUND);
        }
        return systemUser.getRoles().stream()
                .map(BaseEntity::getId).collect(Collectors.toList());
    }

    public void addRole(Long userId, Long roleId) {
        setSystemUser(userId, roleId);
        systemUser.getRoles().add(systemRoleEntity);
        save(systemUser);
    }

    public void deleteRole(Long userId, Long roleId) {
        setSystemUser(userId, roleId);
        systemUser.getRoles().remove(systemRoleEntity);
        save(systemUser);
    }

    private void setSystemUser(Long userId, Long roleId) {
        systemUser = findOne(userId).orElse(null);
        if (systemUser == null) {
            throw new SystemException(SystemResultEnum.USER_NOT_FOUND);
        }
        systemRoleEntity = systemRoleDao.findById(roleId).orElse(null);
        if (systemRoleEntity == null) {
            throw new SystemException(SystemResultEnum.ROLE_NOT_FOUND);
        }
    }
}
