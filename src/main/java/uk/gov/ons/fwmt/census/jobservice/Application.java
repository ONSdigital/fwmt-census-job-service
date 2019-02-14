package uk.gov.ons.fwmt.census.jobservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.retry.annotation.EnableRetry;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableRetry
@EnableSwagger2
@ComponentScan({"uk.gov.ons.fwmt.census.jobservice", "uk.gov.ons.fwmt.census.events"})
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
