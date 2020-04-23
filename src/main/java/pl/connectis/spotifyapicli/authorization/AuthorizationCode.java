package pl.connectis.spotifyapicli.authorization;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class AuthorizationCode {

    private String code;
}
