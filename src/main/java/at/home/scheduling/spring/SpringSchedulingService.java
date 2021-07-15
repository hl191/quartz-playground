package at.home.scheduling.spring;

import at.home.job.SimpleTaskComponent;
import at.home.scheduling.fixed.SchedulingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SpringSchedulingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringSchedulingService.class);

    @Autowired
    private SimpleTaskComponent simpleTaskComponent;

    @Scheduled(cron = SchedulingService.SCHEDULE_EVERY_MINUTE)
    void executeTask() {
        LOGGER.info("Starting task by Spring scheduling");
        simpleTaskComponent.execute();
    }
}
