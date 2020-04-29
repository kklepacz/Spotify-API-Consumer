package pl.connectis.spotifyapicli;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pl.connectis.spotifyapicli.APICalls.AlbumsApiCall;
import pl.connectis.spotifyapicli.APICalls.ApiCallerFactory;
import pl.connectis.spotifyapicli.APICalls.ArtistsApiCall;
import pl.connectis.spotifyapicli.authorization.AuthorizationStrategy;
import pl.connectis.spotifyapicli.authorization.TokenService;

import java.util.Arrays;

@Lazy
@Component
@Slf4j
@Profile({"!test"})
public class SpotifyAPICLR implements CommandLineRunner {

    private final AuthorizationStrategy authorization;
    private final ApiCallerFactory apiCallerFactory;


    public SpotifyAPICLR(@Qualifier("AuthorizationStrategy") AuthorizationStrategy authorization, ApiCallerFactory apiCallerFactory, TokenService tokenService) {
        this.authorization = authorization;
        this.apiCallerFactory = apiCallerFactory;
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


        log.info("Parsed args: {}", Arrays.toString(cmd.getArgs()));

        Option option = cmd.getOptions()[0];
        if (cmd.hasOption("a")) {
            authorization.authorize();
            log.info("Authorization done. Token generated.");
        } else if (cmd.getArgs().length > 0) {
            log.info("found args");
            if (cmd.hasOption("ab") && cmd.getArgs()[0].equals("tracks")) {
                String id = cmd.getOptionValue(option.getOpt());
                log.info("Parsed id: {}", id);
                log.info(((AlbumsApiCall) apiCallerFactory.getCaller(option.getLongOpt())).getAlbumTracks(id).toString());
            } else if (cmd.hasOption("at")) {
                String id = cmd.getOptionValue(option.getOpt());
                log.info("Parsed id: {}", id);
                switch (cmd.getArgs()[0]) {
                    case "albums":
                        log.info(((ArtistsApiCall) apiCallerFactory.getCaller(option.getLongOpt())).getArtistAlbums(id).toString());
                        break;
                    case "top-tracks":
                        String countryCode = cmd.getArgs()[1];
                        log.info("{}", ((ArtistsApiCall) apiCallerFactory.getCaller(option.getLongOpt())).getArtistTracks(id, countryCode));
                        break;
                    case "related-artists":
                        log.info("{}", ((ArtistsApiCall) apiCallerFactory.getCaller(option.getLongOpt())).getArtistRelatedArtists(id));
                        break;
                }
            }
        } else {
            String ids = cmd.getOptionValue(option.getOpt());
            log.info("Parsed ids: {}", ids);
            log.info("{}", apiCallerFactory.getCaller(option.getLongOpt()).getList(ids));
        }
        System.exit(0); //for servlet application type
    }

    private Options buildOptions() {
        final Options options = new Options();
        options.addOption("a", "authorize", false, "Authorize with Spotify API");
        options.addOption("tr", "track", true, "Get track/s info. Examples: -tr 11dFghVXANMlKmJXsNCb ," +
                "--track 11dFghVXANMlKmJXsNCb,20I6sIOMTCkB6w7ryavxtO");
        options.addOption("ab", "album", true, "Get album/s info. Optional args: tracks Examples: -ab 41MnTivkwTO3UUJ8DrqEJJ ," +
                "--album 41MnTivkwTO3UUJ8DrqEJJ,6JWc4iAiJ9FjyK0B59ABb4,6UXCm6bOO4gFlDQZV5yL37");
        options.addOption("at", "artist", true, "Get artist/s info. Optional arg:  Examples: -at 0OdUWJ0sBjDrqHygGUXeCF, " +
                "--artist 0oSGxfWSnnOXhD2fKuz2Gy,3dBVyJ7JuOMt4GE9607Qin");
        return options;
    }


}
