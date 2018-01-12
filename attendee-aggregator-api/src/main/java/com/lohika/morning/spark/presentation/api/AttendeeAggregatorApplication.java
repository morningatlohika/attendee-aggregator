package com.lohika.morning.spark.presentation.api;

import com.lohika.morning.spark.presentation.spark.driver.configuration.SparkContextConfiguration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan("com.lohika.morning.spark.presentation.api.*")
@Import(SparkContextConfiguration.class)
public class AttendeeAggregatorApplication {

  public static final String APP_HOME = "app.home";

  public static void main(String[] args) {
    checkAndSetHomeDirectory();
    SpringApplication.run(AttendeeAggregatorApplication.class, args);
  }

  static void checkAndSetHomeDirectory() {
    String mcHomeProp = System.getProperty(APP_HOME);
    if (mcHomeProp == null) {
      String userDir = System.getProperty("user.dir");
      System.setProperty(APP_HOME, userDir);
    }
    Logger log = LoggerFactory.getLogger(AttendeeAggregatorApplication.class);
    log.info("Attendee aggregator home location: {}", System.getProperty(APP_HOME));
  }
}
