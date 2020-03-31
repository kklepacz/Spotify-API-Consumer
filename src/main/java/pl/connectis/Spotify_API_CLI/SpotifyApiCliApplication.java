package pl.connectis.Spotify_API_CLI;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@Slf4j
@SpringBootApplication
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