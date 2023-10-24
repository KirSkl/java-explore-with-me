package ru.practicum.service.user;

import ru.practicum.dto.NewUserRequest;
import ru.practicum.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(NewUserRequest newUserRequest);

    List<UserDto> getUsers(int positionToPage, int size, List<Integer> ids);

    void deleteUser(Long userId);
}
