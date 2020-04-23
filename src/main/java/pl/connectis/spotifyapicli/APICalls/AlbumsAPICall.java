package pl.connectis.spotifyapicli.APICalls;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import pl.connectis.spotifyapicli.dto.Album;

import java.util.Arrays;
import java.util.Map;

@Slf4j
@Component
public class AlbumsAPICall implements APICaller {

    private final RestTemplate restTemplate;
    private final HttpHeaders httpHeaders;


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
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(httpHeaders);
        log.info("Headers: {} ", httpHeaders);
        log.info("Entity:{}, Args:{}", httpEntity.toString(), ids);
        String uri = "/albums/{ids}";
        log.info(uri);
        ResponseEntity<Album> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                httpEntity,
                Album.class, ids);
        return response.getStatusCode() == HttpStatus.OK ? response.getBody() : null;
    }

    public Map<String, Album[]> getAlbums(String ids) {
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(httpHeaders);
        log.info("Entity:{}, Args:{}", httpEntity.toString(), ids);
        String uri = "/albums?ids={ids}";
        ResponseEntity<Map<String, Album[]>> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                httpEntity,
                new ParameterizedTypeReference<Map<String, Album[]>>() {
                }, ids);
        return response.getStatusCode() == HttpStatus.OK ? response.getBody() : null;
    }
}
