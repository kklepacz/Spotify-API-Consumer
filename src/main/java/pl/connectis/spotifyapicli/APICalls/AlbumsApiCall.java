package pl.connectis.spotifyapicli.APICalls;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import pl.connectis.spotifyapicli.dto.Album;
import pl.connectis.spotifyapicli.dto.PagingObject;
import pl.connectis.spotifyapicli.dto.Track;

@Slf4j
public class AlbumsApiCall extends BaseApiCaller<Album> {

    public static final String URI_ALBUMS = "/albums?ids={ids}";
    public static final String URI_ALBUMS_TRACKS = "https://api.spotify.com/v1/albums/{id}/tracks";

    public AlbumsApiCall(RestTemplate restTemplate, HttpHeaders httpHeaders) {
        super(restTemplate, httpHeaders, URI_ALBUMS);
    }

    public PagingObject<Track> getAlbumTracks(String id) {
        return getPage(URI_ALBUMS_TRACKS, id);
    }
}
