package pl.connectis.spotifyapicli.APICalls;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pl.connectis.spotifyapicli.dto.Track;

import java.util.Arrays;
import java.util.Map;

@Slf4j
@Component
public class TracksAPICall extends BaseAPICaller<Track> implements APICaller {

    public TracksAPICall(RestTemplate restTemplate, HttpHeaders httpHeaders) {
        super(restTemplate, httpHeaders, Track.class, "/tracks/{id}", "/tracks?ids={ids}");
    }

    public void call(String ids) {
        if (ids.contains(",")) {
            Map<String, Track[]> albums = getMany(ids);
            log.info("Track array toString: {}", Arrays.toString(albums.get("albums")));
        } else {
            Track track = getOne(ids);
            if (track == null) {
                log.info("No body in response");
            } else {
                log.info("Track toString: {}", track.toString());
            }
        }
    }
}

