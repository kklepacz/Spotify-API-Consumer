package pl.connectis.spotifyapicli.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PagingObject<T> {
    private String href;
    private List<T> items;
    private Integer limit;
    private String next;
    private Integer offset;
    private String previous;
    private Integer total;
}
