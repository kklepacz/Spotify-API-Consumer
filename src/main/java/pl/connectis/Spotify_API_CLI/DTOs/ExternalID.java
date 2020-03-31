package pl.connectis.Spotify_API_CLI.DTOs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalID {

    private String isrc;
    private String ean;
    private String upc;
}
