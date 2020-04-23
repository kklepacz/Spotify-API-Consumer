package pl.connectis.spotifyapicli.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Copyright {

    private String text;
    private String type;
}
