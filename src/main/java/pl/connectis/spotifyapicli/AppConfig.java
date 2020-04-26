package pl.connectis.spotifyapicli;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;
import pl.connectis.spotifyapicli.authorization.AuthorizationStrategy;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Lazy
@Configuration
public class AppConfig {

    private final ApplicationContext context;

    public AppConfig(ApplicationContext context) {
        this.context = context;
    }

    @Bean
    public AuthorizationStrategy AuthorizationStrategy(@Value("${auth.authorizationMethod}") String qualifier) {
        return (AuthorizationStrategy) context.getBean(qualifier);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder, @Value("${baseUrl}") String baseUrl) {
        return builder.rootUri(baseUrl).build();
    }

    @Lazy
    @Bean
    public HttpHeaders httpHeaders() {
        log.info("HttpHeaders init.");
        return new HttpHeaders();
    }

    @Bean
    public ReentrantLock lock() {
        return new ReentrantLock();
    }

    @Bean
    public Condition condition() {
        return lock().newCondition();
    }

}
