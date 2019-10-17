package cn.hengyumo.humor.cysz.stats.req;

import cn.hengyumo.humor.base.mvc.BaseService;
import cn.hengyumo.humor.base.utils.date.Week;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * CyszDailyReqService
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/10/13
 */
@Service
public class CyszDailyReqService extends BaseService<CyszDailyReq, Long> {

    @Resource
    private CyszDailyReqDao cyszDailyReqDao;


    public List<CyszDailyReqVo> getWeekReqBar() {
        List<CyszDailyReqVo> weekReqStats = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 2;
        if (dayOfWeek == -1) {
            // 周日
            dayOfWeek = 6;
        }
        // 获取这周1
        calendar.add(Calendar.DATE, - dayOfWeek);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        for (Week week : Week.values()) {
            Date preDate = calendar.getTime();
            calendar.add(Calendar.DATE, 1);
            Date afterDate = calendar.getTime();
            // System.out.println(week.getChineseName());
            // System.out.println(preDate);
            // System.out.println(afterDate);
            CyszDailyReq cyszDailyReq = cyszDailyReqDao
                    .getByIsDeletedFalseAndDateBetween(preDate, afterDate);
            if (cyszDailyReq == null) {
                CyszDailyReqVo cyszDailyReqVo = new CyszDailyReqVo();
                cyszDailyReqVo.setName(week.getChineseName());
                cyszDailyReqVo.setReqNum(0L);
                weekReqStats.add(cyszDailyReqVo);
            }
            else {
                CyszDailyReqVo cyszDailyReqVo = new CyszDailyReqVo();
                cyszDailyReqVo.setName(week.getChineseName());
                cyszDailyReqVo.setReqNum(cyszDailyReq.getReqNum());
                weekReqStats.add(cyszDailyReqVo);
            }
        }
        return weekReqStats;
    }

    public CyszDailyReq getTodayReqStats() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date1 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date date2 = calendar.getTime();
        return cyszDailyReqDao.getByIsDeletedFalseAndDateBetween(date1, date2);
    }

    public Long getTodayReqNum() {
        CyszDailyReq cyszDailyReq = getTodayReqStats();
        if (cyszDailyReq == null) {
            return 0L;
        }
        return cyszDailyReq.getReqNum();
    }

    public void addOneTodayReq() {
        // 今日0时-明日0时
        CyszDailyReq cyszDailyReq = getTodayReqStats();
        if (cyszDailyReq == null) {
            cyszDailyReq = new CyszDailyReq();
            cyszDailyReq.setReqNum(1L);
            cyszDailyReq.setDate(new Date());
        }
        else {
            cyszDailyReq.setReqNum(cyszDailyReq.getReqNum() + 1);
        }
        save(cyszDailyReq);
    }

    public Long getAllReqNum() {
        Long allReqNum = 0L;
        List<CyszDailyReq> cyszDailyReqs = findAll();
        for (CyszDailyReq cyszDailyReq : cyszDailyReqs) {
            allReqNum += cyszDailyReq.getReqNum();
        }
        return allReqNum;
    }
}
