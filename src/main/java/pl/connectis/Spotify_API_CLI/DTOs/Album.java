package pl.connectis.Spotify_API_CLI.DTOs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Album {

    private String album_type;
    private List<Artist> artists;
    private List<String> available_markets;
    private List<Copyright> copyrigths;
    private ExternalID external_ids;
    private ExternalURL external_urls;
    private List<String> genres;
    private String href;
    private String id;
    private List<Image> images;
    private String name;
    private Integer popularity;
    private String release_date;
    private String release_date_precision;
    private PagingObject<List<Track>> tracks;
    private String type;
    private String uri;
}
