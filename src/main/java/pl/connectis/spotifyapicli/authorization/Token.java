package pl.connectis.spotifyapicli.authorization;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Slf4j
public class Token implements Serializable {

    private final Long generationTime = System.currentTimeMillis();

    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("token_type")
    private String tokenType;
    private String scope;
    @JsonProperty("expires_in")
    private Integer expiresInSecond;
    private Long expirationTimeMillis;
    @JsonProperty("refresh_token")
    private String refreshToken;

    public Token() {
    }

    public void setExpirationTimeInMillisecondsBasedOnGenerationTimeAndExpiresInSeconds() {
        this.expirationTimeMillis = this.generationTime - 30000L + this.expiresInSecond * 1000L; // 30 seconds overhead
    }

}
