package at.home.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QuartzTask implements Job {

    public static final String EXECUTION_ID = "executionId";
    private static final Logger LOGGER = LoggerFactory.getLogger(QuartzTask.class);

    @Autowired
    private SimpleTaskComponent simpleTaskComponent;

    @Override
    public void execute(JobExecutionContext context) {
        LOGGER.info("Executing Job with description={}, {}={}",
                    context.getJobDetail().getDescription(),
                    EXECUTION_ID,
                    context.getMergedJobDataMap().getLongFromString(EXECUTION_ID));
        simpleTaskComponent.execute();
    }
}
