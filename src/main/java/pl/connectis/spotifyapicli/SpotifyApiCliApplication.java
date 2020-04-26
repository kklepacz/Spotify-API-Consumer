package pl.connectis.spotifyapicli;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import pl.connectis.spotifyapicli.authorization.AuthorizationConfig;

@Slf4j
@SpringBootApplication
@EnableConfigurationProperties(AuthorizationConfig.class)
public class SpotifyApiCliApplication {

    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(SpotifyApiCliApplication.class);
        log.info("arg 0: {}", args[0]);
        builder.headless(false);
        builder.run(args);
    }

}