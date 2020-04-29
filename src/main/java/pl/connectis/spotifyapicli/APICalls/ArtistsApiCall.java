package pl.connectis.spotifyapicli.APICalls;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import pl.connectis.spotifyapicli.dto.*;

import java.util.List;

@Slf4j
public class ArtistsApiCall extends BaseApiCaller<Artist> {

    private static final String URI_ARTISTS = "/artists?ids={ids}";
    private static final String URI_ARTISTS_ALBUMS = "/artists/{id}/albums";
    private static final String URI_ARTISTS_TOP_TRACKS = "/artists/{id}/top-tracks?country={countryCode}";
    private static final String URI_ARTISTS_RELATED_ARTISTS = "/artists/{id}/related-artists";

    public ArtistsApiCall(RestTemplate restTemplate, HttpHeaders httpHeaders) {
        super(restTemplate, httpHeaders, URI_ARTISTS);
    }

    public PagingObject<Album> getArtistAlbums(String id) {
        return getPage(URI_ARTISTS_ALBUMS, id);
    }

    public List<Track> getArtistTracks(String id, String countryCode) {
        return getMany(URI_ARTISTS_TOP_TRACKS, id, countryCode);
    }

    public List<Artist> getArtistRelatedArtists(String id) {
        return getMany(URI_ARTISTS_RELATED_ARTISTS, id);
    }
}
