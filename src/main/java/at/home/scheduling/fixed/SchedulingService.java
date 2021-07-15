package at.home.scheduling.fixed;

import at.home.job.QuartzTask;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;

@Service
public class SchedulingService {

    public static final String SCHEDULE_EVERY_MINUTE = "0 * * ? * *";

    @Autowired
    private Scheduler scheduler;

    @PostConstruct
    void setupFixedSchedule() throws SchedulerException {
        JobDetail job = JobBuilder.newJob()
                                  .ofType(QuartzTask.class)
                                  .storeDurably()
                                  .withIdentity("FIXED_JOB")
                                  .withDescription("Fixed Job")
                                  .setJobData(new JobDataMap(Map.of(QuartzTask.EXECUTION_ID, "3")))
                                  .build();
        Trigger trigger = TriggerBuilder.newTrigger()
                                        .forJob(job)
                                        .withSchedule(CronScheduleBuilder.cronSchedule(SCHEDULE_EVERY_MINUTE))
                                        .build();
        scheduler.scheduleJob(job, trigger);
    }
}
