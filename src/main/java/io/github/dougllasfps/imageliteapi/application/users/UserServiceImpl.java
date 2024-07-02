package io.github.dougllasfps.imageliteapi.application.users;

import io.github.dougllasfps.imageliteapi.application.jwt.JwtService;
import io.github.dougllasfps.imageliteapi.domain.AccessToken;
import io.github.dougllasfps.imageliteapi.domain.entity.User;
import io.github.dougllasfps.imageliteapi.domain.exception.DuplicatedTupleException;
import io.github.dougllasfps.imageliteapi.domain.service.UserService;
import io.github.dougllasfps.imageliteapi.infra.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public User save(User user) {
        User possibleUser = getByEmail(user.getEmail());
        if (possibleUser != null) {
            throw new DuplicatedTupleException("User already exists");
        }
        encodePassword(user);
        return userRepository.save(user);
    }

    @Override
    public AccessToken authenticate(String email, String rawPassword) {
        User user = getByEmail(email);
        if (user == null) {
            return null;
        }

        boolean matches = passwordEncoder.matches(rawPassword, user.getPassword());
        if (matches) {
            return jwtService.generateAccessToken(user);
        }

        return null;
    }

    private void encodePassword(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

}
