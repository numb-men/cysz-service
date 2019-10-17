package cn.hengyumo.humor.base.mvc;


import cn.hengyumo.humor.base.exception.common.BaseException;
import cn.hengyumo.humor.base.exception.enums.BaseResultEnum;
import cn.hengyumo.humor.base.system.user.SystemCurrentUser;
import cn.hengyumo.humor.base.utils.BaseUtil;
import cn.hengyumo.humor.cysz.user.CyszCurrentUser;
import cn.hengyumo.humor.wx.user.WxCurrentUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * UserAuditor
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/9
 */
@Slf4j
@Configuration
public class UserAuditor implements AuditorAware<Long> {

    @Resource
    private BaseUtil baseUtil;


    /**
     * 通过对当前请求用户获取数据，来设置createBy、updateBy属性
     *
     * @return Optional<Long>
     */
    @Override
    public Optional<Long> getCurrentAuditor() {
        Long userId = null;
        if (baseUtil.currentUserIsAdmin() || baseUtil.currentUserIsSystemUser()) {
            SystemCurrentUser currentUser = baseUtil.getSystemCurrentUser();
            userId = currentUser.getId();
        }
        else if (baseUtil.currentUserIsWxUser()) {
            WxCurrentUser wxCurrentUser = baseUtil.getWxCurrentUser();
            if (wxCurrentUser.getId() == null && wxCurrentUser.getWxUser() == null
                    && wxCurrentUser.getWxOpenId() != null) {
                // first save
                userId = baseUtil.getSystemAdmin().getId();
            }
            else {
                userId = wxCurrentUser.getId();
            }
        }
        else if (baseUtil.currentUserIsCyszUser()) {
            CyszCurrentUser cyszCurrentUser = baseUtil.getCyszCurrentUser();
            if (cyszCurrentUser.getId() == null) {
                // first save
                userId = baseUtil.getSystemAdmin().getId();
            }
            else {
                userId = cyszCurrentUser.getId();
            }
        }
        if (userId == null) {
            userId = baseUtil.getSystemAdmin().getId();
            // throw new BaseException(BaseResultEnum.SET_AUDITOR_FAIL);
        }
        return Optional.of(userId);
    }
}
