package pl.connectis.Spotify_API_CLI.APICalls;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;
import pl.connectis.Spotify_API_CLI.authorization.Token;

import java.security.InvalidParameterException;

@Configuration
public class APICallerFactory {

    private final RestTemplate restTemplate;
    private final HttpHeaders httpHeaders;
    private final Token token;

    public APICallerFactory(RestTemplate restTemplate, HttpHeaders httpHeaders, Token token) {
        this.restTemplate = restTemplate;
        this.httpHeaders = httpHeaders;
        this.token = token;
    }

    @Bean
    public IAPICaller album() {
        return new AlbumsAPICall(restTemplate, httpHeaders);
    }

    @Bean
    public IAPICaller track() {
        return new TracksAPICall(restTemplate, httpHeaders);
    }


    public IAPICaller getCaller(String arg) {
        httpHeaders.setBearerAuth(token.getAccess_token());
        switch (arg) {
            case "album":
                return album();
            case "track":
                return track();
            default:
                throw new InvalidParameterException("Wrong argument provided.");
        }
    }
}
