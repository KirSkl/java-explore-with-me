package ru.practicum.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.dto.user.NewUserRequest;
import ru.practicum.dto.user.UserDto;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.mapper.UserMapper;
import ru.practicum.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public UserDto createUser(NewUserRequest newUserRequest) {
        return UserMapper.toUserDto(repository.save(UserMapper.UserRequestToUser(newUserRequest)));
    }

    @Override
    public List<UserDto> getUsers(Pageable page, List<Integer> ids) {
        if (!ids.isEmpty()) {
            return repository.getUsersByIdIn(ids).stream().map(UserMapper::toUserDto).collect(Collectors.toList());
        }
        return repository.getAll(page).stream().map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Long userId) {
        if (repository.deleteByIdAndReturnCount(userId) != 1) {
            throw new NotFoundException("Пользователь не найден");
        }
    }
}