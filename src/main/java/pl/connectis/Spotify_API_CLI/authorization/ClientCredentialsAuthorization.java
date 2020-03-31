package pl.connectis.Spotify_API_CLI.authorization;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${authorizationMethod}")
    private String authorizationMethod;
    @Value("${clientId}")
    private String clientID;
    @Value("${clientSecret}")
    private String clientSecret;
    @Value("${accessTokenUri}")
    private String accessTokenUri;


    public ClientCredentialsAuthorization(ApplicationContext context, RestTemplate restTemplate, HttpHeaders httpHeaders) {
        this.context = context;
        this.restTemplate = restTemplate;
        this.httpHeaders = httpHeaders;
    }

    public void authorize() {

        HttpEntity<MultiValueMap<String, String>> httpEntity;

        String toEncodeClientIdAndSecret = clientID + ":" + clientSecret;
        String encodedClientIdAndSecret = Base64.getEncoder().encodeToString(toEncodeClientIdAndSecret.getBytes());

        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set("Content-Type", "application/x-www-form-urlencoded");
        httpHeaders.setBasicAuth(encodedClientIdAndSecret);
        MultiValueMap<String, String> values = new LinkedMultiValueMap<>();
        values.add("grant_type", authorizationMethod);
        httpHeaders.putAll(values);

        log.info(httpHeaders.toString());

        httpEntity = new HttpEntity<>(values, httpHeaders);

        Token newToken = restTemplate.postForObject(accessTokenUri,
                httpEntity,
                Token.class);
        log.info(newToken.toString());
        newToken = newToken.saveToken();
        Token tokenBeanToUpdate = context.getBean(Token.class);
        updateTokenBean(tokenBeanToUpdate, newToken);
    }

    private void updateTokenBean(Token tokenBean, Token newToken) {
        tokenBean.setAccess_token(newToken.getAccess_token());
        tokenBean.setExpires_in(newToken.getExpires_in());
        tokenBean.setExpirationTimeMillis(newToken.getExpirationTimeMillis());
        tokenBean.setToken_type(newToken.getToken_type());
        log.info("BeanToken: {}", tokenBean.toString());
    }
}
