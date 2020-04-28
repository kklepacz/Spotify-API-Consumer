package pl.connectis.spotifyapicli.authorization;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;


@Slf4j
@Component("client_credentials")
public class ClientCredentialsAuthorization implements AuthorizationStrategy {

    private final RestTemplate restTemplate;
    private final AuthorizationConfig authorizationConfig;

    public ClientCredentialsAuthorization(RestTemplate restTemplate, AuthorizationConfig authorizationConfig) {
        this.restTemplate = restTemplate;
        this.authorizationConfig = authorizationConfig;
    }


    public Token authorize() {

        final HttpHeaders httpHeaders = new HttpHeaders();
        HttpEntity<MultiValueMap<String, String>> httpEntity;

        String toEncodeClientIdAndSecret = authorizationConfig.getClientID() + ":" + authorizationConfig.getClientSecret();
        String encodedClientIdAndSecret = Base64.getEncoder().encodeToString(toEncodeClientIdAndSecret.getBytes());

        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set("Content-Type", "application/x-www-form-urlencoded");
        httpHeaders.setBasicAuth(encodedClientIdAndSecret);
        MultiValueMap<String, String> values = new LinkedMultiValueMap<>();
        values.add("grant_type", authorizationConfig.getAuthorizationMethod());
        httpHeaders.putAll(values);

        log.info(httpHeaders.toString());

        httpEntity = new HttpEntity<>(values, httpHeaders);

        Token newToken = restTemplate.postForObject(authorizationConfig.getAccessTokenUri(),
                httpEntity,
                Token.class);
        if (newToken == null)
            throw new IllegalStateException("Could not receive token information from api. Try again. Check connection status.");
        newToken.setExpirationTimeInMillisecondsBasedOnGenerationTimeAndExpiresInSeconds();
        log.info("ReceivedToken: {} ", newToken.toString());
        return newToken;
    }
}
