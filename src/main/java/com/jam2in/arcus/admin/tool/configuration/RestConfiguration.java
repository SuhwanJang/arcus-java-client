package com.jam2in.arcus.admin.tool.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestConfiguration {

  private final MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

  public RestConfiguration(MappingJackson2HttpMessageConverter converter) {
    this.mappingJackson2HttpMessageConverter = converter;
  }

  @Bean
  public RestTemplate restTemplate() {
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.getMessageConverters().add(mappingJackson2HttpMessageConverter);

    return restTemplate;
  }

}
