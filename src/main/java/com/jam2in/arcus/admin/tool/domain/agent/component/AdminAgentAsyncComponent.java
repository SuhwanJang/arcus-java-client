package com.jam2in.arcus.admin.tool.domain.agent.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jam2in.arcus.admin.tool.domain.agent.dto.MemcachedOptionsDto;
import com.jam2in.arcus.admin.tool.error.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Component
@Slf4j
public class AdminAgentAsyncComponent {

  private static final String START_ZOOKEEPER_API_FORMAT = "http://%s/api/v1/zkservers/%d/start";
  private static final String STOP_ZOOKEEPER_API_FORMAT = "http://%s/api/v1/zkservers/%d/stop";
  private static final String START_MEMCACHED_API_FORMAT = "http://%s/api/v1/mcservers/%d/start";
  private static final String STOP_MEMCACHED_API_FORMAT = "http://%s/api/v1/mcservers/%d/stop";

  private final RestTemplate restTemplate;
  private final ObjectMapper objectMapper;

  public AdminAgentAsyncComponent(RestTemplate restTemplate, ObjectMapper objectMapper) {
    this.restTemplate = restTemplate;
    this.objectMapper = objectMapper;
  }

  @Async
  public CompletableFuture<ApiError> startZooKeeperServer(String address, int port, String token) {
    return post(String.format(START_ZOOKEEPER_API_FORMAT, address, port),
        new HttpEntity<>(makeHeaders(token)));
  }

  @Async
  public CompletableFuture<ApiError> stopZooKeeperServer(String address, int port, String token) {
    return post(String.format(STOP_ZOOKEEPER_API_FORMAT, address, port),
        new HttpEntity<>(makeHeaders(token)));
  }

  @Async
  public CompletableFuture<ApiError> startMemcachedServer(String address, int port, String token,
                                                          MemcachedOptionsDto memcachedOptionsDto) {
    return post(String.format(START_MEMCACHED_API_FORMAT, address, port),
        new HttpEntity<>(memcachedOptionsDto, makeHeaders(token)));
  }

  @Async
  public CompletableFuture<ApiError> stopMemcachedServer(String address, int port, String token) {
    return post(String.format(STOP_MEMCACHED_API_FORMAT, address, port),
        new HttpEntity<>(makeHeaders(token)));
  }

  private CompletableFuture<ApiError> post(String url, HttpEntity<?> entity) {
    try {
        restTemplate.postForEntity(url, entity, ApiError.class);
    } catch (RestClientException restClientException) {
      try {
        if (restClientException instanceof RestClientResponseException) {
          ApiError apiError = objectMapper.readValue(
              ((RestClientResponseException) restClientException).getResponseBodyAsString(),
              ApiError.class);
          return CompletableFuture.completedFuture(apiError);
        } else {
          throw restClientException;
        }
      } catch (Exception e) {
        throw new CompletionException(e);
      }
    }

    return CompletableFuture.completedFuture(null);
  }

  private HttpHeaders makeHeaders(String token) {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(List.of(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(token);
    headers.setContentType(MediaType.APPLICATION_JSON);

    return headers;
  }

}
