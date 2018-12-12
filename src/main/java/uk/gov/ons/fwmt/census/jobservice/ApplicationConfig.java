package uk.gov.ons.fwmt.census.jobservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@Slf4j
@SpringBootApplication
@EnableRetry
public class ApplicationConfig {

  public static void main(String[] args) {
    SpringApplication.run(ApplicationConfig.class, args);
    log.debug("Started application");
  }
}
