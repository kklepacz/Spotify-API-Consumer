package pl.connectis.Spotify_API_CLI;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pl.connectis.Spotify_API_CLI.APICalls.APICallerFactory;
import pl.connectis.Spotify_API_CLI.authorization.AuthorizationStrategy;
import pl.connectis.Spotify_API_CLI.authorization.Token;

import java.io.IOException;
import java.util.Arrays;

@Component
@Slf4j
@Profile({"!test"})
public class SpotifyAPICLR implements CommandLineRunner {

    //    @Value("${authorizationMethod}")
//    static private String authorizationMethod;
    @Qualifier("${authorizationMethod}")
    private final AuthorizationStrategy authorization;
    private final APICallerFactory apiCallerFactory;
    private Token token;


    public SpotifyAPICLR(AuthorizationStrategy authorization, APICallerFactory apiCallerFactory, Token token) {
        this.authorization = authorization;
        this.apiCallerFactory = apiCallerFactory;
        this.token = token;
    }

    public void run(String... args) {

        log.info("args: {}", Arrays.toString(args));

        Options options = new CLIOptionsBuilder().build();
        CommandLineParser CLIParser = new DefaultParser();
        CommandLine cmd;
        try {
            cmd = CLIParser.parse(options, args);
        } catch (ParseException ex) {
            log.error("Parse error: " + ex.getMessage());
            ex.printStackTrace();
            return;
        }


        if (!cmd.hasOption("a") && (!token.hasToken() || !token.isTokenValid())) {
            authorize(authorization);
        }

        if (cmd.hasOption("a")) {
            authorize(authorization);

        } else {
            String ids = cmd.getOptionValue(cmd.getOptions()[0].getOpt());
            log.info("Parsed ids: {}", ids);
            apiCallerFactory.getCaller(cmd.getOptions()[0].getLongOpt()).call(ids);
        }
        System.exit(0);
    }

    private void authorize(AuthorizationStrategy authorization) {
        try {
            authorization.authorize();
        } catch (IOException ex) {
            log.error(ex.getMessage());
            System.exit(-1);
        }
    }
}
