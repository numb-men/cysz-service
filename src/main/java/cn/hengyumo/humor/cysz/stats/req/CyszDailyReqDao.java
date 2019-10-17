package cn.hengyumo.humor.cysz.stats.req;

import cn.hengyumo.humor.base.mvc.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * CyszDailyReqDao
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/10/13
 */
@Repository
public interface CyszDailyReqDao extends BaseDao<CyszDailyReq, Long> {

    CyszDailyReq getByIsDeletedFalseAndDateBetween(Date date1, Date date2);
}
