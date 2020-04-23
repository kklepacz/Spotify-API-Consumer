package pl.connectis.spotifyapicli.authorization;

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
        newToken.setAccessToken("ThisIsMockTokenValue");
        newToken.setExpiresInSecond(3600);
        newToken.setTokenType("Bearer");
    }

    @Test
    public void ReadAndSaveTokenSame() {
        newToken.saveToken();
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
        newToken.saveToken();

        assertTrue(validToken.isTokenValid());
    }


}
