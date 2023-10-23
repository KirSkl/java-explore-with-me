package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.NewUserRequest;
import ru.practicum.dto.UserDto;
import ru.practicum.model.User;

@UtilityClass
public class UserMapper {

    public User UserRequestToUser(NewUserRequest userRequest) {
        return new User(
                null,
                userRequest.getName(),
                userRequest.getEmail()
        );
    }

    public UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
