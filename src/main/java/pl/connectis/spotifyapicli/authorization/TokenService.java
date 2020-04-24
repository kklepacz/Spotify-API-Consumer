package pl.connectis.spotifyapicli.authorization;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;

@Slf4j
@Component
public class TokenService {

    private final File codeFile = new File("token");

    public void saveTokenToFile(Token token) {

        token.setExpirationTimeInMillisecondsBasedOnGenerationTimeAndExpiresInSeconds();

        try {
            FileOutputStream fileOut = new FileOutputStream(codeFile);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(token);
            out.close();
            fileOut.close();
        } catch (IOException ex) {
            log.error("Saving file error " + ex.getMessage() + '\n' + "Path: " + codeFile.getPath());
        }
    }

    //TODO don't like it I have to every time load Token from file if i want to access it, singleton or bean it anyway? but from bean i have to update values, maybe some lazy load bean?
    public Token readTokenFromFile() {
        Token tokenToRead;
        FileInputStream fileIn;
        ObjectInputStream in;

        try {
            fileIn = new FileInputStream(codeFile);
        } catch (FileNotFoundException ex) {
            log.warn("File not found. " + ex.getMessage() + '\n' + "Path: " + codeFile.getPath());
            log.info("Created empty Token object.");
            return new Token();
        }

        try {
            in = new ObjectInputStream(fileIn);
            tokenToRead = (Token) in.readObject();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            log.info("Created empty Token object.");
            return new Token();
        } catch (IOException ex) {
            log.warn("Reading file operation error. " + ex.getMessage() + '\n' + "Path: " + codeFile.getAbsolutePath());
            log.info("Created empty Token object.");
            return new Token();
        }

        try {
            in.close();
            fileIn.close();
        } catch (IOException ex) {
            log.warn("Closing file operation error. " + ex.getMessage() + '\n' + "Path: " + codeFile.getAbsolutePath());
        }

        return tokenToRead;
    }

    //TODO not safe, just checking if such named file exists, doesn't confirm if the file really contains Token data, it's OK for now
    public boolean fileExists() {
        return codeFile.exists();
    }

    public boolean isTokenValid(Token token) {
        if (!fileExists()) return false;
        if (token.getExpirationTimeMillis() == null) return false;
        return token.getExpirationTimeMillis() > System.currentTimeMillis() - 1000L;
    }


}
