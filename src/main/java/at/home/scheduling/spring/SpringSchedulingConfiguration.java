package at.home.scheduling.spring;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;

@Configuration
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "5m")
public class SpringSchedulingConfiguration {

    /**
     * Using Scheduling Lock Lib:
     * https://github.com/lukas-krecan/ShedLock
     * <p>
     * Needs Database Table as defined in schema.sql (is configurable by the library)
     */
    @Bean
    public LockProvider lockProvider(DataSource dataSource) {
        return new JdbcTemplateLockProvider(JdbcTemplateLockProvider.Configuration.builder()
                                                                                  .withJdbcTemplate(new JdbcTemplate(
                                                                                          dataSource))
                                                                                  .usingDbTime()
                                                                                  .build());
    }
}
