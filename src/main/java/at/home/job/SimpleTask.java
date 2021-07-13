package at.home.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SimpleTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleTask.class);

    public void execute() {
        LOGGER.info("Executing SimpleTask");
    }
}
