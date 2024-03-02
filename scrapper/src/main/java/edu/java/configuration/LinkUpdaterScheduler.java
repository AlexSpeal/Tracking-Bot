package edu.java.configuration;

import org.jboss.logging.Logger;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class LinkUpdaterScheduler {
    private final Logger logger = Logger.getLogger(LinkUpdaterScheduler.class);

    @Scheduled(fixedDelayString = "#{scheduler.interval}")
    public void update() {
        logger.info("update!!!");
    }
}
