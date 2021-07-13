package at.home.scheduling;

import at.home.job.Task;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class JobProviderService {

    private static final List<JobDetail> JOBS = new ArrayList<>();

    static {
        JOBS.add(JobBuilder.newJob()
                           .ofType(Task.class)
                           .storeDurably()
                           .withIdentity("JOB_NR_1")
                           .withDescription("Job nummeros uno ...")
                           .build());
        JOBS.add(JobBuilder.newJob()
                           .ofType(Task.class)
                           .storeDurably()
                           .withIdentity("JOB_NR_2")
                           .withDescription("Job nummeros dos ...")
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
