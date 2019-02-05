package uk.gov.ons.fwmt.census.jobservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

  @Value("${totalmobile.username}")
  private String userName;
  @Value("${totalmobile.password}")
  private String password;

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
    return restTemplateBuilder
        .basicAuthentication(userName, password)
        .interceptors()
        .build();
  }
}
