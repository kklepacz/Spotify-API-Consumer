package pl.connectis.spotifyapicli.APICalls;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import pl.connectis.spotifyapicli.authorization.TokenService;

import java.security.InvalidParameterException;

@Slf4j
@Lazy
@Component
public class APICallerFactory {

    private final HttpHeaders httpHeaders;
    private final TokenService tokenService;
    private final APICaller album;
    private final APICaller track;
    private final APICaller artist;

    public APICallerFactory(HttpHeaders httpHeaders, TokenService tokenService, AlbumsAPICall album, TracksAPICall track, ArtistAPICall artist) {
        this.httpHeaders = httpHeaders;
        this.tokenService = tokenService;
        this.album = album;
        this.track = track;
        this.artist = artist;
        log.info("ApiCallerFactory init.");
    }

    public APICaller getCaller(String arg) {
        httpHeaders.setBearerAuth(tokenService.readTokenFromFile().getAccessToken());
        log.info("HttpHeaders filled with Bearer.");
        switch (arg) {
            case "album":
                return album;
            case "track":
                return track;
            case "artist":
                return artist;
            default:
                throw new InvalidParameterException("Wrong argument provided.");
        }
    }
}
