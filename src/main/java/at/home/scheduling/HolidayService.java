package at.home.scheduling;

import org.quartz.DateBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.calendar.HolidayCalendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.Date;

@RestController
public class HolidayService {

    static final String HOLIDAYS_CALENDAR_NAME = "holidays";

    @Autowired
    private Scheduler scheduler;

    @PostConstruct
    void setupHolidays() throws SchedulerException {
        HolidayCalendar holidayCalendar = new HolidayCalendar();

        holidayCalendar.addExcludedDate(DateBuilder.newDate().inMonthOnDay(DateBuilder.AUGUST, 1).build());
        holidayCalendar.addExcludedDate(DateBuilder.newDate().inMonthOnDay(DateBuilder.DECEMBER, 25).build());
        holidayCalendar.addExcludedDate(DateBuilder.newDate().inMonthOnDay(DateBuilder.DECEMBER, 26).build());

        scheduler.addCalendar(HOLIDAYS_CALENDAR_NAME, holidayCalendar, true, true);
    }

    @PostMapping("addHoliday")
    public void addHoliday(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) throws
                                                                                                   SchedulerException {
        HolidayCalendar holidayCalendar = (HolidayCalendar) scheduler.getCalendar(HOLIDAYS_CALENDAR_NAME);
        holidayCalendar.addExcludedDate(date);
        scheduler.addCalendar(HOLIDAYS_CALENDAR_NAME, holidayCalendar, true, true);
    }
}
