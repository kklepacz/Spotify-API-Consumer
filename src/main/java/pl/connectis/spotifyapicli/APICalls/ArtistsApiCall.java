package pl.connectis.spotifyapicli.APICalls;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import pl.connectis.spotifyapicli.dto.Album;
import pl.connectis.spotifyapicli.dto.Artist;
import pl.connectis.spotifyapicli.dto.PagingObject;
import pl.connectis.spotifyapicli.dto.Track;

import java.util.Arrays;
import java.util.Map;

@Slf4j
@Lazy
public class ArtistsApiCall extends BaseApiCaller<Artist> implements ApiCaller {

    private final RestTemplate restTemplate;
    private final HttpHeaders httpHeaders;


    public ArtistsApiCall(RestTemplate restTemplate, HttpHeaders httpHeaders) {
        super(restTemplate, httpHeaders, Artist.class, "/artists/{id}", "/artists?ids={ids}");
        this.restTemplate = restTemplate;
        this.httpHeaders = httpHeaders;
        log.info("ArtistApiCall init.");
    }

    public void call(String ids) {
        if (ids.contains(",")) {
            Map<String, Artist[]> artists = getMany(ids);
            log.info("Artist array toString: {}", Arrays.toString(artists.get("artist")));
        } else {
            Artist artist = getOne(ids);
            log.info("Artist toString: {}", artist.toString());
        }
    }

    public PagingObject<Album> getManyAlbumsFromOneArtist(String id) {
        String uriManyAlbumsFromOneArtist = "https://api.spotify.com/v1/artists/{id}/albums";
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<PagingObject<Album>> response = restTemplate.exchange(
                uriManyAlbumsFromOneArtist,
                HttpMethod.GET,
                httpEntity,
                new ParameterizedTypeReference<PagingObject<Album>>() {
                }, id);
        return response.getStatusCode() == HttpStatus.OK ? response.getBody() : null;
    }

    public Map<String, Track[]> getManyTopTracksFromOneArtist(String id, String countryCode) {
        String uriTopTracksFromOneArtist = "https://api.spotify.com/v1/artists/{id}/top-tracks?country={countryCode}";
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<Map<String, Track[]>> response = restTemplate.exchange(
                uriTopTracksFromOneArtist,
                HttpMethod.GET,
                httpEntity,
                new ParameterizedTypeReference<Map<String, Track[]>>() {
                }, id, countryCode);
        return response.getStatusCode() == HttpStatus.OK ? response.getBody() : null;
    }

    public Map<String, Artist[]> getManyRelatedArtistsFromOneArtist(String id) {
        String uriRelatedArtistsFromOneArtist = "https://api.spotify.com/v1/artists/{id}/related-artists";
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<Map<String, Artist[]>> response = restTemplate.exchange(
                uriRelatedArtistsFromOneArtist,
                HttpMethod.GET,
                httpEntity,
                new ParameterizedTypeReference<Map<String, Artist[]>>() {
                }, id);
        return response.getStatusCode() == HttpStatus.OK ? response.getBody() : null;
    }


}
