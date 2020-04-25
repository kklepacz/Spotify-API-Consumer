package pl.connectis.spotifyapicli.authorization;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Component
public class TokenService {

    private final String fileName = "token";
    private final Path path = Paths.get(fileName);

    public void saveTokenToFile(Token token) {

        try {
            OutputStream fileOut = Files.newOutputStream(path);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(token);
            out.close();
            fileOut.close();
        } catch (IOException ex) {
            log.error("Saving file error " + ex.getMessage() + '\n' + "Path: " + path);
        }
    }

    //TODO don't like it I have to every time load Token from file if i want to access it, singleton or bean it anyway? but from bean i have to update values, maybe some lazy load bean?
    public Token readTokenFromFile() {
        Token tokenToRead;
        InputStream fileIn;
        ObjectInputStream in;

        try {
            fileIn = Files.newInputStream(path);
            in = new ObjectInputStream(fileIn);
            tokenToRead = (Token) in.readObject();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            log.info("Created empty Token object.");
            return new Token();
        } catch (IOException ex) {
            log.warn("Reading file operation error.", ex);
            log.info("Created empty Token object.");
            return new Token();
        }

        try {
            in.close();
            fileIn.close();
        } catch (IOException ex) {
            log.warn("Closing file operation error.", ex);
        }

        return tokenToRead;
    }

    //TODO not safe, just checking if such named file exists, doesn't confirm if the file really contains Token data, it's OK for now
    public boolean fileExists() {
        return Files.exists(path);
    }

    public boolean isTokenValid(Token token) {
        if (!fileExists()) return false;
        if (token.getExpirationTimeMillis() == null) return false;
        return token.getExpirationTimeMillis() > System.currentTimeMillis() - 1000L;
    }


}
