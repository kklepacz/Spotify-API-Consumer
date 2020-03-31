package pl.connectis.Spotify_API_CLI.APICalls;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import pl.connectis.Spotify_API_CLI.DTOs.Album;
import pl.connectis.Spotify_API_CLI.authorization.AuthorizationStrategy;
import pl.connectis.Spotify_API_CLI.authorization.Token;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class AlbumsAPICallTest {

    @Autowired
    private APICallerFactory apiCallerFactory;
    @Autowired
    @Qualifier("client_credentials")
    private AuthorizationStrategy authorization;
    @Autowired
    private Token token;
    @Autowired
    private HttpHeaders httpHeaders;


    private String ids1 = "41MnTivkwTO3UUJ8DrqEJJ";
    private String ids2 = "41MnTivkwTO3UUJ8DrqEJJ,6JWc4iAiJ9FjyK0B59ABb4,6UXCm6bOO4gFlDQZV5yL37";


    @Test
    public void TestIfSpecifiedByIDAlbumGetsReturned() throws IOException {
//        if (!token.hasToken() || !token.isTokenValid()) {
        authorization.authorize();
//        }
        httpHeaders.setBearerAuth(token.getAccess_token());
        Album album = ((AlbumsAPICall) apiCallerFactory.album()).getOneAlbum(ids1);
        assertEquals(ids1, album.getId());
    }

}
