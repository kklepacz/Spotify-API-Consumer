package pl.connectis.spotifyapicli;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;
import pl.connectis.spotifyapicli.authorization.Token;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Configuration
public class AppConfig {
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder, @Value("${baseUrl}") String baseUrl) {
        return builder.rootUri(baseUrl).build();
    }

    @Bean
    public Token token() {
        return new Token().readToken();
    }

    @Bean
    public HttpHeaders httpHeaders() {
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
