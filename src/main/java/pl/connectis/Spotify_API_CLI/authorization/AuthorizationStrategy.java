package pl.connectis.Spotify_API_CLI.authorization;

import java.io.IOException;

public interface AuthorizationStrategy {
    void authorize() throws IOException;
}
