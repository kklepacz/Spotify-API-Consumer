package pl.connectis.spotifyapicli.APICalls;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import pl.connectis.spotifyapicli.authorization.TokenService;

import java.security.InvalidParameterException;

@Lazy
@Component
public class APICallerFactory {

    private final HttpHeaders httpHeaders;
    private final TokenService tokenService;
    private final APICaller album;
    private final APICaller track;

    public APICallerFactory(HttpHeaders httpHeaders, TokenService tokenService, AlbumsAPICall album, TracksAPICall track) {
        this.httpHeaders = httpHeaders;
        this.tokenService = tokenService;
        this.album = album;
        this.track = track;
    }


    public APICaller getCaller(String arg) {
        httpHeaders.setBearerAuth(tokenService.readTokenFromFile().getAccessToken());
        switch (arg) {
            case "album":
                return album;
            case "track":
                return track;
            default:
                throw new InvalidParameterException("Wrong argument provided.");
        }
    }
}
