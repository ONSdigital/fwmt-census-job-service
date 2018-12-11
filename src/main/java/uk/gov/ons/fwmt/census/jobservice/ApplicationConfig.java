package uk.gov.ons.fwmt.census.jobservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.client.RestTemplate;

@Slf4j
@SpringBootApplication
@Configuration
@EnableRetry
public class ApplicationConfig {

  @Value("${service.mock.username}")
  private String userName;
  @Value("${service.mock.password}")
  private String password;

  public static void main(String[] args) {
    SpringApplication.run(ApplicationConfig.class, args);
    log.debug("Started application");
  }

  @Bean
  public RestTemplate RestTemplate(RestTemplateBuilder restTemplateBuilder) {
    return restTemplateBuilder
        .basicAuthentication(userName, password)
        .interceptors()
        .build();
  }
}
