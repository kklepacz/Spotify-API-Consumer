package pl.connectis.spotifyapicli.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Track {

    private Album album;
    private List<Artist> artists;
    @JsonProperty("available_markets")
    private List<String> availableMarkets;
    @JsonProperty("disc_number")
    private Integer discNumber;
    @JsonProperty("duration_ms")
    private Integer durationMs;
    private Boolean explicit;
    @JsonProperty("external_ids")
    private ExternalID externalIds;
    @JsonProperty("external_urls")
    private ExternalURL externalUrls;
    private String href;
    private String id;
    @JsonProperty("is_playable")
    private Boolean isPlayable;
    @JsonProperty("linked_from")
    private Track linkedFrom;
    private Restrictions restrictions;
    private String name;
    private Integer popularity;
    @JsonProperty("preview_url")
    private String previewUrl;
    @JsonProperty("track_number")
    private Integer trackNumber;
    private String type;
    private String uri;
}
