package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.DTO.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    List<UserDto> findAll();

    UserDto getUser(Long id);

    User getUserEntity(long id);

    UserDto addUser(UserDto userDto);

    UserDto updateUser(UserDto userDto, long id);

    void deleteUser(long id);
}
