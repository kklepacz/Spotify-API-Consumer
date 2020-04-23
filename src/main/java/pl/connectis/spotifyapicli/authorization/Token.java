package pl.connectis.spotifyapicli.authorization;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Slf4j
public class Token implements Serializable {

    private final Long generationTime = System.currentTimeMillis();
    private final File codeFile = new File("token");
    @JsonProperty("access_token")
    private String accessToken;
    private String tokenType;
    private String scope;
    @JsonProperty("expires_in")
    private Integer expiresInSecond;
    private Long expirationTimeMillis;
    @JsonProperty("refresh_token")
    private String refreshToken;

    public Token() {
    }

    public void setExpirationTimeInMillisecondsBasedOnGenerationTimeAndExpiresInSeconds() {
        this.expirationTimeMillis = this.generationTime - 30000L + this.expiresInSecond * 1000L; // 30 seconds overhead
    }

    public void saveToken() {
        Token tokenToSave = this;
        setExpirationTimeInMillisecondsBasedOnGenerationTimeAndExpiresInSeconds();

        try {
            FileOutputStream fileOut = new FileOutputStream(codeFile);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(tokenToSave);
            out.close();
            fileOut.close();
        } catch (IOException ex) {
            log.error("Saving file error " + ex.getMessage() + '\n' + "Path: " + codeFile.getPath());
        }
    }

    public Token readToken() {
        Token tokenToRead = new Token();
        FileInputStream fileIn;
        ObjectInputStream in;

        try {
            fileIn = new FileInputStream(codeFile);
        } catch (FileNotFoundException ex) {
            log.warn("File not found. " + ex.getMessage() + '\n' + "Path: " + codeFile.getPath());
            log.info("Created empty Token object.");
            return tokenToRead;
        }

        try {
            in = new ObjectInputStream(fileIn);
            tokenToRead = (Token) in.readObject();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            log.info("Created empty Token object.");
            return tokenToRead;
        } catch (IOException ex) {
            log.warn("Reading file operation error. " + ex.getMessage() + '\n' + "Path: " + codeFile.getAbsolutePath());
            log.info("Created empty Token object.");
            return tokenToRead;
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
    public boolean hasToken() {
        return codeFile.exists();
    }

    public boolean isTokenValid() {
        if (!hasToken()) return false;
        if (expirationTimeMillis == null) return false;
        return expirationTimeMillis > System.currentTimeMillis() - 1000L;
    }


}
