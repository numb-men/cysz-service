package cn.hengyumo.humor.wx.user;


import cn.hengyumo.humor.base.mvc.BaseDao;
import org.springframework.stereotype.Repository;

/**
 * WxUserDao
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/14
 */
@Repository
public interface WxUserDao extends BaseDao<WxUser, Long> {

    WxUser findByOpenId(String openId);
}
