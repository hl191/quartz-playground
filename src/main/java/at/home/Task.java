package at.home;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Task implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(Task.class);

    @Override
    public void execute(JobExecutionContext context) {
        LOGGER.info("Task executed");
    }
}
