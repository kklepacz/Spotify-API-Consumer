package pl.connectis.spotifyapicli.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Album {

    @JsonProperty("album_type")
    private String albumType;
    private List<Artist> artists;
    @JsonProperty("available_markets")
    private List<String> availableMarkets;
    private List<Copyright> copyrights;
    @JsonProperty("external_ids")
    private ExternalID externalIds;
    @JsonProperty("external_urls")
    private ExternalURL externalUrls;
    private List<String> genres;
    private String href;
    private String id;
    private List<Image> images;
    private String name;
    private Integer popularity;
    @JsonProperty("release_date")
    private String releaseDate;
    @JsonProperty("release_date_precision")
    private String releaseDatePrecision;
    private PagingObject<List<Track>> tracks;
    private String type;
    private String uri;
}
