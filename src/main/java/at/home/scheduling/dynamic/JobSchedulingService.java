package at.home.scheduling.dynamic;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.quartz.CalendarIntervalScheduleBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import at.home.converter.DateConverter;

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
    public List<TriggerExecutionTime> getExecutionTimesForJob(@RequestParam String jobKey) throws SchedulerException {
        JobDetail job = getJobDetailForKey(jobKey);
        return scheduler.getTriggersOfJob(job.getKey())
                        .stream()
                        .map(i -> new TriggerExecutionTime(i.getKey().toString(), i.getStartTime()))
                        .collect(Collectors.toList());
    }

    @DeleteMapping("removeExecutionTimeForJob")
    public void removeExecutionTimeForJob(@RequestParam String jobKey, @RequestParam String triggerKey) throws SchedulerException {
        JobDetail job = getJobDetailForKey(jobKey);
        Trigger triggerToRemove = scheduler.getTriggersOfJob(job.getKey()).stream().filter(i -> i.getKey().toString().equals(triggerKey)).findFirst().orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND, "TriggerKey not found for job"));
        scheduler.unscheduleJob(triggerToRemove.getKey());
    }

    @PostMapping("pauseScheduler")
    public void pauseScheduler(@RequestParam(required = false) Optional<String> jobKey) throws SchedulerException {
        if (jobKey.isPresent()) {
            scheduler.pauseJob(getJobDetailForKey(jobKey.get()).getKey());
        } else {
            scheduler.pauseAll();
        }
    }

    @PostMapping("resumeScheduler")
    public void resumeScheduler(@RequestParam(required = false) Optional<String> jobKey) throws SchedulerException {
        if (jobKey.isPresent()) {
            scheduler.resumeJob(getJobDetailForKey(jobKey.get()).getKey());
        } else {
            scheduler.resumeAll();
        }
    }

    private JobDetail getJobDetailForKey(String jobKey) throws SchedulerException {
        return Objects.requireNonNull(scheduler.getJobDetail(new JobKey(jobKey)), "JobKey unknown");
    }
}
