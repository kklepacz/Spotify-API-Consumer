package pl.connectis.spotifyapicli.APICalls;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pl.connectis.spotifyapicli.authorization.TokenService;

import java.security.InvalidParameterException;

@Slf4j
@Component
public class ApiCallerFactory {

    private final RestTemplate restTemplate;
    private final TokenService tokenService;

    public ApiCallerFactory(RestTemplate restTemplate, TokenService tokenService) {
        this.restTemplate = restTemplate;
        this.tokenService = tokenService;
        log.info("ApiCallerFactory init.");
    }

    public ApiCaller getCaller(String arg) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(tokenService.getToken().getAccessToken());
        log.info("HttpHeaders filled with Bearer.");
        switch (arg) {
            case "album":
                return new AlbumsApiCall(restTemplate, httpHeaders);
            case "track":
                return new TracksApiCall(restTemplate, httpHeaders);
            case "artist":
                return new ArtistsApiCall(restTemplate, httpHeaders);
            default:
                throw new InvalidParameterException("Wrong argument provided.");
        }
    }
}
