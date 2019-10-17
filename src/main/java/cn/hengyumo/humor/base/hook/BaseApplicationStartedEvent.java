package cn.hengyumo.humor.base.hook;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;

/**
 * BaseApplicationStartingEvent
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/10
 */
@Slf4j
public class BaseApplicationStartedEvent implements ApplicationListener<ApplicationStartedEvent> {

    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
        log.info("spring boot 启动完成");
    }
}
