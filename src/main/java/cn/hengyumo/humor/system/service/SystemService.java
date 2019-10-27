package cn.hengyumo.humor.system.service;


import cn.hengyumo.humor.system.config.SystemConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * SystemService
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/10
 */
@Service
@Slf4j
public class SystemService implements SystemConfig {

    @Resource
    private SystemResourceService systemResourceService;

    @Resource
    private SystemDictService systemDictService;

    public void clearResources() {
        systemResourceService.clearResources();
    }

    public void createResources(HttpServletRequest request) {
        systemResourceService.createResources(request);
    }

    public void createDict() {
        systemDictService.createDict();
    }

    public void clearDict() {
        systemDictService.clearDict();
    }
}
