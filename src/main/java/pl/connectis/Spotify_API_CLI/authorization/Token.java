package pl.connectis.Spotify_API_CLI.authorization;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@Slf4j
public class Token implements Serializable {

    private final Long generation_time = System.currentTimeMillis();
    private final File codeFile = new File("token");
    private String access_token;
    private String token_type;
    private String scope;
    private Integer expires_in; //Seconds
    private Long expirationTimeMillis;
    private String refresh_token;

    public Token() {
    }

    public void setExpirationTimeMillis() {
        this.expirationTimeMillis = this.generation_time - 30000L + this.expires_in * 1000L; // 30 seconds overhead
    }

    public Token saveToken() {
        Token tokenToSave = this;
        setExpirationTimeMillis();

        try {
            FileOutputStream fileOut = new FileOutputStream(codeFile);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(tokenToSave);
            out.close();
            fileOut.close();
        } catch (IOException ex) {
            log.error("Saving file error " + ex.getMessage() + '\n' + "Path: " + codeFile.getPath());
        }
        return tokenToSave;
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
