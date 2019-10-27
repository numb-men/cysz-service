package cn.hengyumo.humor.cysz.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class CyszDailyReqServiceTest {

    @Resource
    private CyszDailyReqService cyszDailyReqService;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getWeekReqBar() {
        log.info(cyszDailyReqService.getWeekReqBar().toString());
    }

    @Test
    public void getTodayReqStats() {
        log.info(cyszDailyReqService.getWeekReqBar().toString());
    }

    @Test
    public void getTodayReqNum() {
        log.info(cyszDailyReqService.getTodayReqStats().toString());
    }

    @Test
    public void addOneTodayReq() {
        long preReq = cyszDailyReqService.getTodayReqNum();
        cyszDailyReqService.addOneTodayReq();
        long nowReq = cyszDailyReqService.getTodayReqNum();
        Assert.assertEquals(preReq + 1, nowReq);
    }

    @Test
    public void getAllReqNum() {
        log.info(cyszDailyReqService.getAllReqNum().toString());
    }
}
