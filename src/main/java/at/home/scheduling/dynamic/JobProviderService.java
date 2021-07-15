package at.home.scheduling.dynamic;

import at.home.job.QuartzTask;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class JobProviderService {

    private static final List<JobDetail> JOBS = new ArrayList<>();

    static {
        JOBS.add(JobBuilder.newJob()
                           .ofType(QuartzTask.class)
                           .storeDurably()
                           .withIdentity("JOB_NR_1")
                           .withDescription("Job nummeros uno ...")
                           .setJobData(new JobDataMap(Map.of(QuartzTask.EXECUTION_ID, "1")))
                           .build());
        JOBS.add(JobBuilder.newJob()
                           .ofType(QuartzTask.class)
                           .storeDurably()
                           .withIdentity("JOB_NR_2")
                           .withDescription("Job nummeros dos ...")
                           .setJobData(new JobDataMap(Map.of(QuartzTask.EXECUTION_ID, "2")))
                           .build());
    }

    @Autowired
    private Scheduler scheduler;

    @PostConstruct
    private void registerJobs() {
        JOBS.forEach(job -> {
            try {
                scheduler.addJob(job, false);
            } catch (SchedulerException e) {
                throw new IllegalStateException(e);
            }
        });
    }
}
