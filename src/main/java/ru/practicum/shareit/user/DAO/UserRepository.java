package ru.practicum.shareit.user.DAO;

import ru.practicum.shareit.user.model.DTO.UserDto;

import java.util.List;

public interface UserRepository {

    List<UserDto> getUsers();

    UserDto getUser(int id);

    UserDto addUser(UserDto userDto);

    UserDto updateUser(UserDto userDto, int id);

    void deleteUser(int id);

    void checkId(int itemId, int userId);

    void addItem(int itemId, int userId);
}
