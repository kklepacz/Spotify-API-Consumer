package pl.connectis.spotifyapicli.APICalls;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public abstract class BaseAPICaller<T> {

    private final RestTemplate restTemplate;
    private final HttpHeaders httpHeaders;
    private final Class<T> clazz;
    private final String uriOne;
    private final String uriMany;

    protected BaseAPICaller(RestTemplate restTemplate, HttpHeaders httpHeaders, Class<T> clazz, String uriOne, String uriMany) {
        this.restTemplate = restTemplate;
        this.httpHeaders = httpHeaders;
        this.clazz = clazz;
        this.uriOne = uriOne;
        this.uriMany = uriMany;
    }

    public T getOne(String id) {
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<T> response = restTemplate.exchange(
                uriOne,
                HttpMethod.GET,
                httpEntity,
                clazz
                , id);
        return response.getStatusCode() == HttpStatus.OK ? response.getBody() : null;
    }

    public Map<String, T[]> getMany(String ids) {
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<Map<String, T[]>> response = restTemplate.exchange(
                uriMany,
                HttpMethod.GET,
                httpEntity,
                new ParameterizedTypeReference<Map<String, T[]>>() {
                }, ids);
        return response.getStatusCode() == HttpStatus.OK ? response.getBody() : null;
    }
}
