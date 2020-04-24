package pl.connectis.spotifyapicli.authorization;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TokenTest {

    private Token newToken;
    private TokenService tokenService = new TokenService();

    @BeforeEach
    void init() {
        newToken = new Token();
        newToken.setAccessToken("ThisIsMockTokenValue");
        newToken.setExpiresInSecond(Integer.MAX_VALUE);
        newToken.setExpirationTimeInMillisecondsBasedOnGenerationTimeAndExpiresInSeconds();
        newToken.setTokenType("Bearer");
    }

    @Test
    public void ReadAndSaveTokenSame() {
        tokenService.saveTokenToFile(newToken);
        Token readToken = tokenService.readTokenFromFile();

        assertEquals(newToken, readToken);
    }

    @Test
    public void TokenHasExpired() {
        Token expiredToken = new Token();
        expiredToken.setExpirationTimeMillis(System.currentTimeMillis() - 1000L);

        assertFalse(tokenService.isTokenValid(expiredToken));
    }

    @Test
    public void TokenIsValid() {
        assertTrue(tokenService.isTokenValid(newToken));
    }


}
