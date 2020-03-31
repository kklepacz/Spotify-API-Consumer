package pl.connectis.Spotify_API_CLI;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class CLIOptionsBuilder {

    private static final Options options = new Options();

    public Options build() {
        Option option1 = Option.builder("a").longOpt("authorize").hasArg(true).optionalArg(true).valueSeparator(' ').build();
        options.addOption(option1);
        options.addOption("tr", "track", true, "Get track/tracks info. Examples: -tr 11dFghVXANMlKmJXsNCb ," +
                "--track 11dFghVXANMlKmJXsNCb,20I6sIOMTCkB6w7ryavxtO");
        options.addOption("ab", "album", true, "Get album/albums info. Examples: -ab 41MnTivkwTO3UUJ8DrqEJJ ," +
                "--album 41MnTivkwTO3UUJ8DrqEJJ,6JWc4iAiJ9FjyK0B59ABb4,6UXCm6bOO4gFlDQZV5yL37");
        return options;
    }

}
