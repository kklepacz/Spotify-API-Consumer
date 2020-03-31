package pl.connectis.Spotify_API_CLI.APICalls;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import pl.connectis.Spotify_API_CLI.DTOs.Track;

import java.util.Arrays;
import java.util.Map;

@Slf4j
public class TracksAPICall implements IAPICaller {

    private final RestTemplate restTemplate;
    private final HttpHeaders httpHeaders;
    private HttpEntity<MultiValueMap<String, String>> httpEntity;

    @Value("${baseUrl}")
    private String BASE_URL;

    public TracksAPICall(RestTemplate restTemplate, HttpHeaders httpHeaders) {
        this.restTemplate = restTemplate;
        this.httpHeaders = httpHeaders;
    }

    public void call(String ids) {
        if (ids.contains(",")) {
            Map<String, Track[]> albums = getTracks(ids);
            log.info("Track array toString: {}", Arrays.toString(albums.get("albums")));
        } else {
            Track track = getOneTrack(ids);
            if (track == null) {
                log.info("No body in response");
            } else {
                log.info("Track toString: {}", track.toString());
            }
        }
    }

    public Track getOneTrack(String ids) {
        httpEntity = new HttpEntity<>(httpHeaders);
        log.info("Entity:{}, Args:{}", httpEntity.toString(), ids);
        log.info(BASE_URL + "/tracks/{ids}");
        ResponseEntity<Track> response = restTemplate.exchange(
                BASE_URL + "/tracks/{ids}",
                HttpMethod.GET,
                httpEntity,
                Track.class, ids);
//            log.info(response.toString());
        return response.getStatusCode() == HttpStatus.OK ? response.getBody() : null;
    }

    public Map<String, Track[]> getTracks(String ids) {
        httpEntity = new HttpEntity<>(httpHeaders);
        log.info("Entity:{}, Args:{}", httpEntity.toString(), ids);
        log.info(BASE_URL + "/tracks?ids={ids}");
        ResponseEntity<Map<String, Track[]>> response = restTemplate.exchange(
                BASE_URL + "/tracks?ids={ids}",
                HttpMethod.GET,
                httpEntity,
                new ParameterizedTypeReference<Map<String, Track[]>>() {
                }, ids);
//            log.info(response.toString());
        return response.getStatusCode() == HttpStatus.OK ? response.getBody() : null;
    }
}

