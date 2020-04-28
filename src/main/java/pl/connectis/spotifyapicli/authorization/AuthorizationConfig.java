package pl.connectis.spotifyapicli.authorization;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.context.ApplicationContext;

@Getter
@ConfigurationProperties(prefix = "auth")
@ConstructorBinding
public class AuthorizationConfig {

    private final ApplicationContext context;
    private final String authorizationMethod;
    private final String clientID;
    private final String clientSecret;
    private final String userAuthorizationUri;
    private final String accessTokenUri;

    public AuthorizationConfig(ApplicationContext context, String authorizationMethod, String clientID, String clientSecret, String userAuthorizationUri, String accessTokenUri) {
        this.authorizationMethod = authorizationMethod;
        this.clientID = clientID;
        this.clientSecret = clientSecret;
        this.userAuthorizationUri = userAuthorizationUri;
        this.accessTokenUri = accessTokenUri;
        this.context = context;
    }

}
