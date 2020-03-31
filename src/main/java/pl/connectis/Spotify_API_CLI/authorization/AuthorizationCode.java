package pl.connectis.Spotify_API_CLI.authorization;

import org.springframework.stereotype.Component;

@Component
public class AuthorizationCode {

    private String code;

    public AuthorizationCode() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
