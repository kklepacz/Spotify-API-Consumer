package pl.connectis.spotifyapicli.APICalls;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import pl.connectis.spotifyapicli.authorization.Token;

import java.security.InvalidParameterException;

@Component
public class APICallerFactory {

    private final HttpHeaders httpHeaders;
    private final Token token;
    private final APICaller album;
    private final APICaller track;

    public APICallerFactory(HttpHeaders httpHeaders, Token token, AlbumsAPICall album, TracksAPICall track) {
        this.httpHeaders = httpHeaders;
        this.token = token;
        this.album = album;
        this.track = track;
    }


    public APICaller getCaller(String arg) {
        httpHeaders.setBearerAuth(token.getAccessToken());
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
