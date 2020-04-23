package pl.connectis.spotifyapicli.APICalls;


import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import pl.connectis.spotifyapicli.dto.Track;

import java.util.Arrays;
import java.util.Map;

@Slf4j
@Component
public class TracksAPICall implements APICaller {

    private final RestTemplate restTemplate;
    private final HttpHeaders httpHeaders;

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
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(httpHeaders);
        log.info("Entity:{}, Args:{}", httpEntity.toString(), ids);
        ResponseEntity<Track> response = restTemplate.exchange(
                "/tracks/{ids}",
                HttpMethod.GET,
                httpEntity,
                Track.class, ids);
        return response.getStatusCode() == HttpStatus.OK ? response.getBody() : null;
    }

    public Map<String, Track[]> getTracks(String ids) {
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(httpHeaders);
        log.info("Entity:{}, Args:{}", httpEntity.toString(), ids);
        ResponseEntity<Map<String, Track[]>> response = restTemplate.exchange(
                "/tracks?ids={ids}",
                HttpMethod.GET,
                httpEntity,
                new ParameterizedTypeReference<Map<String, Track[]>>() {
                }, ids);
        return response.getStatusCode() == HttpStatus.OK ? response.getBody() : null;
    }
}

