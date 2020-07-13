package com.anz.circuitbreaker;

import java.time.Duration;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;

// https://resilience4j.readme.io/docs/getting-started-3
@Configuration
public class CircuitBreakerNonReactiveConfig {
  @Bean
  public Customizer<Resilience4JCircuitBreakerFactory> globalCustomConfiguration() {
    TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(20)).build();
    CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom().failureRateThreshold(50).waitDurationInOpenState(Duration.ofMillis(20000)).slidingWindowSize(2).build();
    return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id).timeLimiterConfig(timeLimiterConfig).circuitBreakerConfig(circuitBreakerConfig).build());
  }

  @Bean
  public Customizer<Resilience4JCircuitBreakerFactory> specificCustomConfigurationSub() {
    TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(5)).build();
    CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom().failureRateThreshold(50).waitDurationInOpenState(Duration.ofMillis(1000)).slidingWindowSize(2).build();
    String[] subCircuitBreakers = {"circuitBreaker", "circuitBreaker1", "circuitBreaker2", "circuitBreaker3"};
    return factory -> factory.configure(builder -> builder.circuitBreakerConfig(circuitBreakerConfig).timeLimiterConfig(timeLimiterConfig).build(), subCircuitBreakers);
  }
}
