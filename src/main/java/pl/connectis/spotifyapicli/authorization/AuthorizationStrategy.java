package pl.connectis.spotifyapicli.authorization;

import java.io.IOException;

public interface AuthorizationStrategy {
    void authorize() throws IOException;
}
