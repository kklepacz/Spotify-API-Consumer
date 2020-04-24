package pl.connectis.spotifyapicli.authorization;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

@Slf4j
@RestController
public class AuthorizationController {

    private final AuthorizationCode authorizationCode;
    private final Lock lock;
    private final Condition condition;

    public AuthorizationController(AuthorizationCode authorizationCode, Lock lock, Condition condition) {
        this.authorizationCode = authorizationCode;
        this.lock = lock;
        this.condition = condition;
    }

    @GetMapping("/collect")
    public String collect(@RequestParam(name = "code", required = false) String code,
                          @RequestParam(name = "state", required = false) String state,
                          @RequestParam(name = "error", required = false) String error) {
        lock.lock();
        authorizationCode.setCode(code);
        condition.signal();
        lock.unlock();
        log.info("Authorization code: {}", authorizationCode.getCode());
        return "Authorization code saved. You can close this window";
    }
}
