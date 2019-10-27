package cn.hengyumo.humor.cysz.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
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
public class CyszStatsServiceTest {

    @Resource
    private CyszStatsService cyszStatsService;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getAllOrderNum() {
        log.info(cyszStatsService.getAllOrderNum().toString());
    }

    @Test
    public void getTodayEarnings() {
        log.info(cyszStatsService.getTodayEarnings().toString());
    }

    @Test
    public void getAllEarnings() {
        log.info(cyszStatsService.getAllEarnings().toString());
    }

    @Test
    public void getTodayUserAdd() {
        log.info(cyszStatsService.getTodayUserAdd().toString());
    }

    @Test
    public void getAllUserNum() {
        log.info(cyszStatsService.getAllUserNum().toString());
    }

    @Test
    public void view() {
        log.info(cyszStatsService.view().toString());
    }

    @Test
    public void orderPie() {
        log.info(cyszStatsService.orderPie().toString());
    }

    @Test
    public void getWeekReqBar() {
        log.info(cyszStatsService.getWeekReqBar().toString());
    }

    @Test
    public void getFoodWeekStats() {
        log.info(cyszStatsService.getFoodWeekStats().toString());
    }
}
