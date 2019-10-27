package cn.hengyumo.humor.cysz.service;

import cn.hengyumo.humor.base.mvc.BaseService;
import cn.hengyumo.humor.cysz.dao.CyszDailyReqDao;
import cn.hengyumo.humor.cysz.entity.CyszDailyReq;
import cn.hengyumo.humor.cysz.vo.CyszDailyReqVo;
import cn.hengyumo.humor.utils.date.Week;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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


    /**
     * 获取周请求柱状图绘制所需数据
     *
     * @return 周请求柱状图绘制所需数据
     */
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

    /**
     * 获取今日请求统计
     *
     * @return 今日请求统计
     */
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

    /**
     * 获取今日访问量
     *
     * @return 今日访问量
     */
    public Long getTodayReqNum() {
        CyszDailyReq cyszDailyReq = getTodayReqStats();
        if (cyszDailyReq == null) {
            return 0L;
        }
        return cyszDailyReq.getReqNum();
    }

    /**
     * 记录请求
     */
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

    /**
     * 获取总访问量
     *
     * @return 总访问量
     */
    public Long getAllReqNum() {
        Long allReqNum = 0L;
        List<CyszDailyReq> cyszDailyReqs = findAll();
        for (CyszDailyReq cyszDailyReq : cyszDailyReqs) {
            allReqNum += cyszDailyReq.getReqNum();
        }
        return allReqNum;
    }
}
