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
//        if (authorizationMethod.equals("authorization_code")) {
        builder.headless(false);
//            builder.web(WebApplicationType.SERVLET); //TODO stop web thread/tomcat after authorization OR load on demand new servlet context to do the job, pass token bean and then terminate it?
//       } else {
//        builder.headless(false);
//        builder.web(WebApplicationType.NONE);
//        }
        builder.run(args);
    }

}