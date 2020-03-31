package pl.connectis.Spotify_API_CLI.APICalls;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import pl.connectis.Spotify_API_CLI.DTOs.Album;

import java.util.Arrays;
import java.util.Map;

@Slf4j
@Getter
public class AlbumsAPICall implements IAPICaller {

    private final RestTemplate restTemplate;
    private final HttpHeaders httpHeaders;
    private HttpEntity<MultiValueMap<String, String>> httpEntity;
    private String url;

    @Value("${baseUrl}")
    private String BASE_URL;

    public AlbumsAPICall(RestTemplate restTemplate, HttpHeaders httpHeaders) {
        this.restTemplate = restTemplate;
        this.httpHeaders = httpHeaders;
    }

    public void call(String ids) {
        if (ids.contains(",")) {
            Map<String, Album[]> albums = getAlbums(ids);
            log.info("Album array toString: {}", Arrays.toString(albums.get("albums")));
        } else {
            Album album = getOneAlbum(ids);
            log.info("Album toString: {}", album.toString());
        }
    }

    public Album getOneAlbum(String ids) {
        httpEntity = new HttpEntity<>(httpHeaders);
        log.info("Headers: {} ", httpHeaders);
        log.info("Entity:{}, Args:{}", httpEntity.toString(), ids);
        url = BASE_URL + "/albums/{ids}";
        log.info(url);
        ResponseEntity<Album> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                httpEntity,
                Album.class, ids);
//        log.info(response.toString());
        return response.getStatusCode() == HttpStatus.OK ? response.getBody() : null;
    }

    public Map<String, Album[]> getAlbums(String ids) {
        httpEntity = new HttpEntity<>(httpHeaders);
        log.info("Entity:{}, Args:{}", httpEntity.toString(), ids);
        url = BASE_URL + "/albums?ids={ids}";
        ResponseEntity<Map<String, Album[]>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                httpEntity,
                new ParameterizedTypeReference<Map<String, Album[]>>() {
                }, ids);
//        log.info(response.toString());
        return response.getStatusCode() == HttpStatus.OK ? response.getBody() : null;
    }
}
