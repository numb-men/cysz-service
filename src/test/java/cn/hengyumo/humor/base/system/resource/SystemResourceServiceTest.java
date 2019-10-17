package cn.hengyumo.humor.base.system.resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SystemResourceServiceTest {

    @Resource
    private SystemResourceService systemResourceService;

    @Test
    public void deleteResourcesByIdCascade() {
        systemResourceService.deleteResourcesByIdCascade(264L);
    }

    @Test
    public void getResourcesByIdCascade() {
        List<SystemResourceEntity> resourceEntities =
                systemResourceService.getResourcesByIdCascade(264L);
    }
}
