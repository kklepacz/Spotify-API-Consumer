package pl.connectis.spotifyapicli;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pl.connectis.spotifyapicli.APICalls.APICallerFactory;
import pl.connectis.spotifyapicli.authorization.AuthorizationStrategy;
import pl.connectis.spotifyapicli.authorization.Token;
import pl.connectis.spotifyapicli.authorization.TokenService;

import java.io.IOException;
import java.util.Arrays;

@Component
@Slf4j
@Profile({"!test"})
public class SpotifyAPICLR implements CommandLineRunner {

    private final AuthorizationStrategy authorization;
    private final APICallerFactory apiCallerFactory;
    private final TokenService tokenService;


    public SpotifyAPICLR(@Qualifier("AuthorizationStrategy") AuthorizationStrategy authorization, APICallerFactory apiCallerFactory, TokenService tokenService) {
        this.authorization = authorization;
        this.apiCallerFactory = apiCallerFactory;
        this.tokenService = tokenService;
    }

    public void run(String... args) {

        log.info("args: {}", Arrays.toString(args));

        Options options = buildOptions();
        CommandLineParser CLIParser = new DefaultParser();
        CommandLine cmd;
        try {
            cmd = CLIParser.parse(options, args);
        } catch (ParseException ex) {
            log.error("Parse error: {}", ex.getMessage(), ex);
            return;
        }

        Token token = tokenService.readTokenFromFile();

        if (cmd.hasOption("a") || !tokenService.fileExists() || !tokenService.isTokenValid(token)) {
            authorize(authorization);
        }

        String ids = cmd.getOptionValue(cmd.getOptions()[0].getOpt());
        log.info("Parsed ids: {}", ids);
        apiCallerFactory.getCaller(cmd.getOptions()[0].getLongOpt()).call(ids);

        System.exit(0);
    }

    private Options buildOptions() {
        final Options options = new Options();
        Option option1 = Option.builder("a").longOpt("authorize").hasArg(true).optionalArg(true).valueSeparator(' ').build();
        options.addOption(option1);
        options.addOption("tr", "track", true, "Get track/tracks info. Examples: -tr 11dFghVXANMlKmJXsNCb ," +
                "--track 11dFghVXANMlKmJXsNCb,20I6sIOMTCkB6w7ryavxtO");
        options.addOption("ab", "album", true, "Get album/albums info. Examples: -ab 41MnTivkwTO3UUJ8DrqEJJ ," +
                "--album 41MnTivkwTO3UUJ8DrqEJJ,6JWc4iAiJ9FjyK0B59ABb4,6UXCm6bOO4gFlDQZV5yL37");
        return options;
    }

    private void authorize(AuthorizationStrategy authorization) {
        try {
            authorization.authorize();
        } catch (IOException ex) {
            log.error(ex.getMessage());
            throw new IllegalStateException("Could not authorize");
        }
    }
}
