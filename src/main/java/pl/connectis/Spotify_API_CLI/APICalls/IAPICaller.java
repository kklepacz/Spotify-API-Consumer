package pl.connectis.Spotify_API_CLI.APICalls;

import org.springframework.stereotype.Component;

@Component
public interface IAPICaller {
    void call(String ids);
}
