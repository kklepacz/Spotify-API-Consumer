package pl.connectis.spotifyapicli.APICalls;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pl.connectis.spotifyapicli.dto.Artist;

import java.util.Arrays;
import java.util.Map;

@Slf4j
@Component
public class ArtistAPICall extends BaseAPICaller<Artist> implements APICaller {

    public ArtistAPICall(RestTemplate restTemplate, HttpHeaders httpHeaders) {
        super(restTemplate, httpHeaders, Artist.class, "/artists/{id}", "/artists?ids={ids}");
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
}
