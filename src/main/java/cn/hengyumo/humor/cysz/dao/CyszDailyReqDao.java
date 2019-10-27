package cn.hengyumo.humor.cysz.dao;

import cn.hengyumo.humor.base.mvc.BaseDao;
import cn.hengyumo.humor.cysz.entity.CyszDailyReq;
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

    /**
     * 获取在一段日期之间的请求统计
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return CyszDailyReq
     */
    CyszDailyReq getByIsDeletedFalseAndDateBetween(Date date1, Date date2);
}
