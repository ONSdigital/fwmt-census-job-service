package uk.gov.ons.fwmt.census.jobservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableRetry 
@EnableSwagger2 
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
