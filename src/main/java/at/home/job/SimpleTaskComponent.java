package at.home.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SimpleTaskComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleTaskComponent.class);

    public void execute() {
        LOGGER.info("Executing SimpleTask");
    }
}
