package cn.hengyumo.humor.base.utils;


import cn.hengyumo.humor.base.system.user.SystemAdmin;
import cn.hengyumo.humor.base.system.user.SystemCurrentUser;
import cn.hengyumo.humor.base.system.user.SystemUser;
import cn.hengyumo.humor.cysz.user.CyszCurrentUser;
import cn.hengyumo.humor.cysz.user.CyszUser;
import cn.hengyumo.humor.wx.user.WxCurrentUser;
import cn.hengyumo.humor.wx.user.WxUser;
import cn.hengyumo.humor.wx.user.WxUserDao;
import cn.hengyumo.humor.wx.utils.vo.WxSessionVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * BaseUtil
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/9
 */
@Slf4j
@Component
public class BaseUtil {

    @Resource
    private SystemCurrentUser systemCurrentUser;

    @Resource
    private WxCurrentUser wxCurrentUser;

    @Resource
    private CyszCurrentUser cyszCurrentUser;

    @Resource
    private WxUserDao wxUserDao;

    @Resource
    private SystemAdmin systemAdmin;

    private boolean isAdmin;
    private boolean isSystemUser;
    private boolean isWxUser;
    private boolean isCyszUser;

    public void saveCurrentUser(SystemUser user) {
        log.info("currentUser: " + user.toString());
        systemCurrentUser.setSystemCurrentUser(user);
        isAdmin = isWxUser = isCyszUser = false;
        isSystemUser = true;
    }

    public void setAdmin(SystemAdmin admin) {
        log.info("currentUser: " + admin.toString());
        systemCurrentUser.setSystemCurrentUser(admin);
        isWxUser = isSystemUser = isCyszUser = false;
        isAdmin = true;
    }

    public void setCyszCurrentUser(CyszUser cyszUser) {
        log.info("currentUser: " + cyszUser.getUsername());
        cyszCurrentUser = new CyszCurrentUser(cyszUser);
        isWxUser = isSystemUser = isAdmin = false;
        isCyszUser = true;
    }

    public void setWxCurrentUser(WxSessionVo wxSessionVo) {
        log.info("currentUser: ", wxSessionVo.toString());
        this.wxCurrentUser = new WxCurrentUser();
        wxCurrentUser.setWxOpenId(wxSessionVo.getOpenid());
        wxCurrentUser.setWxSessionKey(wxSessionVo.getSession_key());
        WxUser wxUser = wxUserDao.findByOpenId(wxSessionVo.getOpenid());
        if (wxUser != null) {
            wxCurrentUser.setId(wxUser.getId());
            wxCurrentUser.setWxUser(wxUser);
        }
        isAdmin = isSystemUser = false;
        isWxUser = true;
    }

    public SystemAdmin getSystemAdmin() {
        return systemAdmin;
    }

    public WxCurrentUser getWxCurrentUser() {
        return currentUserIsWxUser() ? wxCurrentUser : null;
    }

    public SystemCurrentUser getSystemCurrentUser() {
        return currentUserIsSystemUser() || currentUserIsAdmin() ? systemCurrentUser : null;
    }

    public CyszCurrentUser getCyszCurrentUser() {
        return currentUserIsCyszUser() ? cyszCurrentUser : null;
    }

    public boolean currentUserIsAdmin() {
        return isAdmin;
    }

    public boolean currentUserIsSystemUser() {
        return isSystemUser;
    }

    public boolean currentUserIsWxUser() {
        return isWxUser;
    }

    public boolean currentUserIsCyszUser() {
        return isCyszUser;
    }
}
