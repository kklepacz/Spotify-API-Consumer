package pl.connectis.spotifyapicli.APICalls;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;
import pl.connectis.spotifyapicli.authorization.AuthorizationStrategy;
import pl.connectis.spotifyapicli.authorization.TokenService;
import pl.connectis.spotifyapicli.dto.Album;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class AlbumsAPICallTest {

    @Autowired
    @Qualifier("client_credentials")
    private AuthorizationStrategy authorization;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private RestTemplate restTemplate;


    private String ids1 = "41MnTivkwTO3UUJ8DrqEJJ";
    private String ids2 = "41MnTivkwTO3UUJ8DrqEJJ,6JWc4iAiJ9FjyK0B59ABb4,6UXCm6bOO4gFlDQZV5yL37";


    @Test
    public void TestIfSpecifiedByIDAlbumGetsReturned() {
        final HttpHeaders httpHeaders = new HttpHeaders();
        authorization.authorize();
        httpHeaders.setBearerAuth(tokenService.getToken().getAccessToken());
        final AlbumsApiCall albumsApiCall = new AlbumsApiCall(restTemplate, httpHeaders);
        Album album = albumsApiCall.getOne(ids1);
        assertEquals(ids1, album.getId());
    }

}
