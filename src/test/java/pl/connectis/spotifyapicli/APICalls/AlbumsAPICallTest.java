package pl.connectis.spotifyapicli.APICalls;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import pl.connectis.spotifyapicli.authorization.AuthorizationStrategy;
import pl.connectis.spotifyapicli.authorization.Token;
import pl.connectis.spotifyapicli.dto.Album;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class AlbumsAPICallTest {

    @Autowired
    private AlbumsAPICall albumsAPICall;
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
        httpHeaders.setBearerAuth(token.getAccessToken());
        Album album = albumsAPICall.getOne(ids1);
        assertEquals(ids1, album.getId());
    }

}
