package at.home.scheduling;

import at.home.converter.DateConverter;
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
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class JobSchedulingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobSchedulingService.class);

    @Autowired
    private Scheduler scheduler;

    @PostMapping("schedule")
    public void schedule(@RequestParam String jobKey,
                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDateTime,
                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Optional<LocalDateTime> endDateTime,
                         @RequestParam(required = false) Optional<Integer> repeatEveryXDay) throws SchedulerException {
        JobDetail job = getJobDetailForKey(jobKey);
        TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger()
                                                               .modifiedByCalendar(HolidayService.HOLIDAYS_CALENDAR_NAME)
                                                               .forJob(job)
                                                               .startAt(DateConverter.from(startDateTime));
        endDateTime.ifPresent(localDateTime -> triggerBuilder.endAt(DateConverter.from(localDateTime)));
        repeatEveryXDay.ifPresent(everyXDay -> triggerBuilder.withSchedule(CalendarIntervalScheduleBuilder.calendarIntervalSchedule()
                                                                                                          .withIntervalInDays(
                                                                                                                  everyXDay)));
        Trigger trigger = triggerBuilder.build();
        LOGGER.info("Scheduled a new trigger name={}", trigger.getKey().getName());
        scheduler.scheduleJob(trigger);
    }

    @GetMapping("checkDateIncludedInCalendar")
    public boolean checkDateIncludedInCalendar(@RequestParam String calendarName,
                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) throws
                                                                                                                       SchedulerException {
        return scheduler.getCalendar(calendarName).isTimeIncluded(date.getTime());
    }

    @GetMapping("getExecutionTimesForJob")
    public List<Date> getExecutionTimesForJob(@RequestParam String jobKey) throws SchedulerException {
        JobDetail job = getJobDetailForKey(jobKey);
        return scheduler.getTriggersOfJob(job.getKey())
                        .stream()
                        .map(Trigger::getStartTime)
                        .collect(Collectors.toList());
    }

    private JobDetail getJobDetailForKey(String jobKey) throws SchedulerException {
        return Objects.requireNonNull(scheduler.getJobDetail(new JobKey(jobKey)), "JobKey unknown");
    }
}
