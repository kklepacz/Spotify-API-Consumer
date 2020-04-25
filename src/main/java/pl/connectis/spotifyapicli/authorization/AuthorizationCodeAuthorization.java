package pl.connectis.spotifyapicli.authorization;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
public class AuthorizationCodeAuthorization implements AuthorizationStrategy {

    private final AuthorizationCode authorizationCode;
    private final RestTemplate restTemplate;
    private final HttpHeaders httpHeaders;
    private final ReentrantLock lock;
    private final Condition condition;
    private final AuthorizationConfig authorizationConfig;
    private final TokenService tokenService;
    @Value("{$spring.main.web-application-type}")
    private String webApplicationType;


    public AuthorizationCodeAuthorization(AuthorizationCode authorizationCode, RestTemplate restTemplate, HttpHeaders httpHeaders, ReentrantLock lock, Condition condition, AuthorizationConfig authorizationConfig, TokenService tokenService) {
        this.authorizationCode = authorizationCode;
        this.restTemplate = restTemplate;
        this.httpHeaders = httpHeaders;
        this.lock = lock;
        this.condition = condition;
        this.authorizationConfig = authorizationConfig;
        this.tokenService = tokenService;
    }

    public void authorize() throws IOException {

        if (!webApplicationType.equals("servlet")) {
            throw new RuntimeException("Wrong spring web application type configuration for authorization_code method of authorization. Please change the method or set spring.main.web-application-type to servlet.");
        }

        HttpEntity<MultiValueMap<String, String>> httpEntity;

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(authorizationConfig.getUserAuthorizationUri())
                .queryParam("client_id", authorizationConfig.getClientID())
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

        String toEncodeClientIdAndSecret = authorizationConfig.getClientID() + ":" + authorizationConfig.getClientSecret();
        String encodedClientIdAndSecret = Base64.getEncoder().encodeToString(toEncodeClientIdAndSecret.getBytes());

        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set("Content-Type", "application/x-www-form-urlencoded");
        httpHeaders.setBasicAuth(encodedClientIdAndSecret);
        MultiValueMap<String, String> values = new LinkedMultiValueMap<>();
        values.add("grant_type", authorizationConfig.getAuthorizationMethod());
        values.add("code", authorizationCode.getCode());
        values.add("redirect_uri", "http://localhost:8080/collect");
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
        tokenService.saveTokenToFile(newToken);
    }

}
