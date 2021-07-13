package at.home.scheduling;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.Objects;

@RestController
public class JobSchedulingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobSchedulingService.class);

    @Autowired
    private Scheduler scheduler;

    @PostMapping("schedule")
    public void schedule(@RequestParam String jobKey,
                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime) throws
                                                                                                                   SchedulerException {
        JobDetail job = Objects.requireNonNull(scheduler.getJobDetail(new JobKey(jobKey)));
        Trigger trigger = TriggerBuilder.newTrigger()
                                        .startAt(Date.from(dateTime.toInstant(OffsetDateTime.now().getOffset())))
                                        .modifiedByCalendar(HolidayService.HOLIDAYS_CALENDAR_NAME)
                                        .forJob(job)
                                        .build();

        LOGGER.info("Scheduled a new trigger name={}", trigger.getKey().getName());
        scheduler.scheduleJob(trigger);
    }

    @GetMapping("checkDateIncludedInCalendar")
    public boolean checkDateIncludedInCalendar(@RequestParam String calendarName,
                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) throws
                                                                                                                       SchedulerException {
        return scheduler.getCalendar(calendarName).isTimeIncluded(date.getTime());
    }
}
