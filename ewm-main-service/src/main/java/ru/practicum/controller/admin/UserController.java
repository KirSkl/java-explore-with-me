package ru.practicum.controller.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.common.Constants;
import ru.practicum.common.PaginationUtil;
import ru.practicum.dto.NewUserRequest;
import ru.practicum.dto.UserDto;
import ru.practicum.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/admin/users")
public class UserController {
    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody NewUserRequest newUserRequest) {
        log.info("Получен запрос POST /admin/user на добавление нового пользователя " + newUserRequest.toString());
        return service.createUser(newUserRequest);
    }

    @GetMapping
    public List<UserDto> getUsers(@RequestParam(required = false) List<Integer> ids,
                                  @PositiveOrZero @RequestParam(required = false, defaultValue = Constants.DEFAULT_FROM)
                                  int from,
                                  @Positive @RequestParam(required = false, defaultValue = Constants.DEFAULT_SIZE)
                                  int size) {
        log.info(String.format("Получен запрос GET /admin/users на получение списка пользователей с id = %s, " +
                "начиная с %s, по %s на странице", ids, from, size));
        return service.getUsers(PaginationUtil.positionToPage(from, size), size, ids);
    }

    @DeleteMapping("/{userId")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PositiveOrZero @PathVariable Long userId) {
        log.info(String.format("Получен запрос DELETE /admin/users на удаление пользователя с id = %s", userId));
        service.deleteUser(userId);
    }
}
