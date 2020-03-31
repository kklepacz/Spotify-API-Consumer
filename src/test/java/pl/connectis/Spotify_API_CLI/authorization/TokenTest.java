package pl.connectis.Spotify_API_CLI.authorization;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TokenTest {

    private static Token newToken;
    private Token expiredToken;
    private Token savedToken;
    private Token readToken;
    private Token validToken;

    @BeforeAll
    static void init() {
        newToken = new Token();
        newToken.setAccess_token("ThisIsMockTokenValue");
        newToken.setExpires_in(3600);
        newToken.setToken_type("Bearer");
    }

    @Test
    public void ReadAndSaveTokenSame() {
        savedToken = newToken.saveToken();
        readToken = new Token().readToken();

        assertEquals(savedToken, readToken);
    }

    @Test
    public void TokenHasExpired() {
        expiredToken = new Token();
        expiredToken.setExpirationTimeMillis(System.currentTimeMillis() - 1000L);

        assertFalse(expiredToken.isTokenValid());
    }

    @Test
    public void TokenIsValid() {
        validToken = newToken.saveToken();

        assertTrue(validToken.isTokenValid());
    }


}
