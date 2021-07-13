package at.home.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Task implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(Task.class);

    @Autowired
    private SimpleTask simpleTask;

    @Override
    public void execute(JobExecutionContext context) {
        LOGGER.info("Task executed");
        simpleTask.execute();
    }
}
