package io.github.dougllasfps.imageliteapi.application.jwt;

import io.github.dougllasfps.imageliteapi.domain.AccessToken;
import io.github.dougllasfps.imageliteapi.domain.entity.User;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    public AccessToken generateAccessToken(User user) {
        return new AccessToken("xxx");
    }
}
