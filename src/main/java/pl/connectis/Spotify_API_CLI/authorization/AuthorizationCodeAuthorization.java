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
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Base64;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Component("authorization_code")
public class AuthorizationCodeAuthorization {

    private final AuthorizationCode authorizationCode;
    private final ApplicationContext context;
    private final RestTemplate restTemplate;
    private final HttpHeaders httpHeaders;
    private final ReentrantLock lock;
    private final Condition condition;
    @Value("${authorizationMethod}")
    private String authorizationMethod;
    @Value("${clientId}")
    private String clientID;
    @Value("${clientSecret}")
    private String clientSecret;
    @Value("${userAuthorizationUri}")
    private String userAuthorizationUri;
    @Value("${accessTokenUri}")
    private String accessTokenUri;
    @Value("{$spring.main.web-application-type}")
    private String webApplicationType;


    public AuthorizationCodeAuthorization(AuthorizationCode authorizationCode, ApplicationContext context, RestTemplate restTemplate, HttpHeaders httpHeaders, ReentrantLock lock, Condition condition) {
        this.authorizationCode = authorizationCode;
        this.context = context;
        this.restTemplate = restTemplate;
        this.httpHeaders = httpHeaders;
        this.lock = lock;
        this.condition = condition;
    }

    public void authorize() throws IOException {

        if (!webApplicationType.equals("servlet")) {
            throw new RuntimeException("Wrong spring web application type configuration for authorization_code method of authorization. Please change the method or set spring.main.web-application-type to servlet.");
        }

        HttpEntity<MultiValueMap<String, String>> httpEntity;

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(userAuthorizationUri)
                .queryParam("client_id", clientID)
                .queryParam("response_type", "code")
                .queryParam("redirect_uri", "http://localhost:8080/collect");
//				.queryParam("state", "")
//				.queryParam("scope", "")
//				.queryParam("show_dialog", "")

        log.info(builder.build().toUri().toString());

        try {
            java.awt.Desktop.getDesktop().browse(builder.build().toUri());
        } catch (IOException ex) {
            log.error("Couldn't open default browser.");
            ex.printStackTrace();
        }

        lock.lock();
        try {
            log.info("Awaiting 30 seconds for authorization code.");
            condition.await(30, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            log.info("Interrupted await for authorization code to be generated.");
            ex.printStackTrace();
            return;
        } finally {
            lock.unlock();
        }

        if (authorizationCode.getCode() == null) throw new IOException("Didn't get authorization code");

        String toEncodeClientIdAndSecret = clientID + ":" + clientSecret;
        String encodedClientIdAndSecret = Base64.getEncoder().encodeToString(toEncodeClientIdAndSecret.getBytes());

        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set("Content-Type", "application/x-www-form-urlencoded");
        httpHeaders.setBasicAuth(encodedClientIdAndSecret);
        MultiValueMap<String, String> values = new LinkedMultiValueMap<>();
        values.add("grant_type", authorizationMethod);
        values.add("code", authorizationCode.getCode());
        values.add("redirect_uri", "http://localhost:8080/collect");
        httpHeaders.putAll(values);

        log.info(httpHeaders.toString());

        httpEntity = new HttpEntity<>(values, httpHeaders);

        Token newToken = restTemplate.postForObject(accessTokenUri,
                httpEntity,
                Token.class);
        log.info("ReceivedToken: {} ", newToken.toString());
        newToken = newToken.saveToken();
        Token tokenBeanToUpdate = context.getBean(Token.class);
        updateTokenBean(tokenBeanToUpdate, newToken);
    }

    private void updateTokenBean(Token tokenBean, Token newToken) {
        tokenBean.setAccess_token(newToken.getAccess_token());
        tokenBean.setExpires_in(newToken.getExpires_in());
        tokenBean.setExpirationTimeMillis(newToken.getExpirationTimeMillis());
        tokenBean.setRefresh_token(newToken.getRefresh_token());
        tokenBean.setScope(newToken.getScope());
        tokenBean.setToken_type(newToken.getToken_type());
        log.info("BeanToken: {}", tokenBean.toString());
    }
}
