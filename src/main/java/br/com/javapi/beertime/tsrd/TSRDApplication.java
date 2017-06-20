package br.com.javapi.beertime.tsrd;

import java.io.IOException;
import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Rômero Ricardo
 *
 */
@EnableScheduling
@SpringBootApplication(scanBasePackages="br.com.javapi.beertime.tsrd")
public class TSRDApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(TSRDApplication.class);

    @Autowired
    private Environment env;

    @PostConstruct
    public void initApplication() throws IOException {
        if (env.getActiveProfiles().length == 0) {
            LOGGER.warn("No Spring profile configured, running with default configuration");
        } else {
            LOGGER.info("Running with Spring profile(s) : {}", Arrays.toString(env.getActiveProfiles()));
        }
    }

    public static void main(String[] args) {
        LOGGER.info("TSRD application starting...");
        SpringApplication application = new SpringApplicationBuilder(TSRDApplication.class).web(false).build();
        application.setAdditionalProfiles(args);
        application.run();
    }
}
