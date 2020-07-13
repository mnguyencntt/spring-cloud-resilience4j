package com.anz.circuitbreaker;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.util.function.Function;
import java.util.function.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AlbumService {
  @SuppressWarnings("rawtypes")
  @Autowired
  private CircuitBreakerFactory circuitBreakerFactory;

  private RestTemplate restTemplate = createRestTemplate();

  public String getAlbumList() {
    CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
    String url = "https://jsonplaceholder.typicode.com/albums";
    log.info(url);
    Supplier<String> toRun = () -> {
      long start = System.currentTimeMillis();
      String response = restTemplate.getForObject(url, String.class);
      System.out.println(System.currentTimeMillis() - start);
      System.out.println(response);
      return response;
    };
    Function<Throwable, String> fallback = throwable -> {
      return getDefaultAlbumList();
    };
    return circuitBreaker.run(toRun, fallback);
  }

  private String getDefaultAlbumList() {
    return "[ { \"userId\": 1, \"id\": 1, \"title\": \"quidem molestiae enim\" }, { \"userId\": 1, \"id\": 2, \"title\": \"sunt qui excepturi placeat culpa\" } ]";
  }

  public static RestTemplate createRestTemplate() {
    final SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
    final Proxy proxy = new Proxy(Type.HTTP, new InetSocketAddress("singtelproxy.net.vic", 80));
    requestFactory.setProxy(proxy);
    return new RestTemplate(requestFactory);
  }
}
