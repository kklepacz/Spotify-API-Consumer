package pl.connectis.spotifyapicli.APICalls;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pl.connectis.spotifyapicli.dto.Album;

import java.util.Arrays;
import java.util.Map;

@Slf4j
@Component
public class AlbumsAPICall extends BaseAPICaller<Album> implements APICaller {


    public AlbumsAPICall(RestTemplate restTemplate, HttpHeaders httpHeaders) {
        super(restTemplate, httpHeaders, Album.class, "/albums/{ids}", "/albums?ids={ids}");
    }

    public void call(String ids) {
        if (ids.contains(",")) {
            Map<String, Album[]> albums = getMany(ids);
            log.info("Album array toString: {}", Arrays.toString(albums.get("albums")));
        } else {
            Album album = getOne(ids);
            log.info("Album toString: {}", album.toString());
        }
    }
}
