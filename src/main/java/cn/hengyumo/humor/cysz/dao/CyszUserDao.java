package cn.hengyumo.humor.cysz.dao;

import cn.hengyumo.humor.base.mvc.BaseDao;
import cn.hengyumo.humor.cysz.entity.CyszUser;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * CyszUserDao
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/10/12
 */
@Repository
public interface CyszUserDao extends BaseDao<CyszUser, Long> {

    /**
     * 根据手机号查找所有用户
     *
     * @param mobile 手机号
     * @return 用户列表
     */
    List<CyszUser> findAllByMobile(String mobile);

    /**
     * 根据手机号查找一个用户
     *
     * @param mobile 手机号
     * @return 用户
     */
    CyszUser findOneByMobile(String mobile);

    /**
     * 根据用户名查找一个用户
     *
     * @param username 用户名
     * @return 用户
     */
    CyszUser findOneByUsername(String username);

    /**
     * 获取在一段日期之内的新增用户数
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 新增用户数
     */
    Long countByIsDeletedFalseAndCreateDateBetween(Date date1, Date date2);
}
