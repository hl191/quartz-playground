package at.home;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobSchedulingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobSchedulingService.class);

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private Task task;

    @PostMapping("schedule")
    public void schedule() throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob()
                                        .ofType(Task.class)
                                        .storeDurably()
                                        .withIdentity("Job Title")
                                        .withDescription("Invoke Sample Job service...")
                                        .build();

        ScheduleBuilder<CalendarIntervalTrigger> calendarIntervalScheduleBuilder = CalendarIntervalScheduleBuilder.calendarIntervalSchedule()
                                                                                                                  .withIntervalInMinutes(
                                                                                                                          1);
        Trigger trigger = TriggerBuilder.newTrigger()
                                        .startAt(DateBuilder.todayAt(17, 55, 0))
                                        .withSchedule(calendarIntervalScheduleBuilder)
                                        .forJob(jobDetail)
                                        .build();

        LOGGER.info("Scheduled a new job with description: {}", jobDetail.getDescription());
        scheduler.scheduleJob(jobDetail, trigger);
    }
}
