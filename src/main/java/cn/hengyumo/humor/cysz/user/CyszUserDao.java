package cn.hengyumo.humor.cysz.user;

import cn.hengyumo.humor.base.mvc.BaseDao;
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

    List<CyszUser> findAllByMobile(String mobile);

    CyszUser findOneByMobile(String mobile);

    CyszUser findOneByUsername(String username);

    Long countByIsDeletedFalseAndCreateDateBetween(Date date1, Date date2);
}
