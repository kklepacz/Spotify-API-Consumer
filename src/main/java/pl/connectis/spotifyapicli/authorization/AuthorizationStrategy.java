package pl.connectis.spotifyapicli.authorization;

public interface AuthorizationStrategy {
    Token authorize();
}
