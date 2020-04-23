package pl.connectis.spotifyapicli.APICalls;


import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;
import pl.connectis.spotifyapicli.dto.Album;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class AlbumsAPICallMockTest {

    @MockBean
    private RestTemplate restTemplate;
    @Autowired
    private APICallerFactory apiCallerFactory;

    @Test
    public void givenRestTemplateMocked_WhenGetOneAlbumInvoked_ThenMockValueReturned() {
        Album albumMock = new Album();
        albumMock.setId("41MnTivkwTO3UUJ8DrqEJJ");
        ResponseEntity<Album> mockResponse = mock(ResponseEntity.class);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Album.class), anyString()))
                .thenReturn(mockResponse);
        when(mockResponse.toString()).thenReturn("MockedResponse");
        when(mockResponse.getStatusCode()).thenReturn(HttpStatus.OK);
        when(mockResponse.getBody()).thenReturn(albumMock);


        Album album = ((AlbumsAPICall) apiCallerFactory.getCaller("album")).getOneAlbum("41MnTivkwTO3UUJ8DrqEJJ");

        Mockito.verify(mockResponse).getStatusCode();
        Mockito.verify(restTemplate).exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Album.class), anyString());

        assertEquals(album.getId(), "41MnTivkwTO3UUJ8DrqEJJ");
    }

}
