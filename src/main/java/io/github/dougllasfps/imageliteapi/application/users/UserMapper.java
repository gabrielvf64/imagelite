package io.github.dougllasfps.imageliteapi.application.users;

import io.github.dougllasfps.imageliteapi.domain.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User mapToUser(UserDTO userDTO) {
        return User.builder()
                .email(userDTO.getEmail())
                .name(userDTO.getName())
                .password(userDTO.getPassword())
                .build();
    }
}
