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
public class Track {

    private Album album;
    private List<Artist> artists;
    private List<String> available_markets;
    private Integer disc_number;
    private Integer duration_ms;
    private Boolean explicit;
    private ExternalID external_ids;
    private ExternalURL external_urls;
    private String href;
    private String id;
    private Boolean is_playable;
    private Track linked_from;
    private Restrictions restrictions;
    private String name;
    private Integer popularity;
    private String preview_url;
    private Integer track_number;
    private String type;
    private String uri;
}
