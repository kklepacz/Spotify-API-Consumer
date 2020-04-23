package pl.connectis.spotifyapicli.authorization;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
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

    private final ApplicationContext context;
    private final RestTemplate restTemplate;
    private final HttpHeaders httpHeaders;
    private final AuthorizationConfig authorizationConfig;


    public ClientCredentialsAuthorization(ApplicationContext context, RestTemplate restTemplate, HttpHeaders httpHeaders, AuthorizationConfig authorizationConfig) {
        this.context = context;
        this.restTemplate = restTemplate;
        this.httpHeaders = httpHeaders;
        this.authorizationConfig = authorizationConfig;
    }

    public void authorize() {

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
        log.info(newToken.toString());
        newToken.saveToken();
        Token tokenBeanToUpdate = context.getBean(Token.class);
        updateTokenBean(tokenBeanToUpdate, newToken);
    }

    private void updateTokenBean(Token tokenBean, Token newToken) {
        tokenBean.setAccessToken(newToken.getAccessToken());
        tokenBean.setExpiresInSecond(newToken.getExpiresInSecond());
        tokenBean.setExpirationTimeMillis(newToken.getExpirationTimeMillis());
        tokenBean.setTokenType(newToken.getTokenType());
        log.info("BeanToken: {}", tokenBean.toString());
    }
}
