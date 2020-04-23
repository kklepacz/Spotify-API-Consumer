package pl.connectis.spotifyapicli.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PagingObject<T> {
    private String href;
    private T items;
    private Integer limit;
    private String next;
    private Integer offset;
    private String previous;
    private Integer total;
}
