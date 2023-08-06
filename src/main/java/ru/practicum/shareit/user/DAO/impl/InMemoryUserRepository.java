package ru.practicum.shareit.user.DAO.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.DAO.UserRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.DTO.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryUserRepository implements UserRepository {

    private HashMap<Integer, User> users = new HashMap<>();
    private UserMapper userMapper;

    @Autowired
    public InMemoryUserRepository(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public List<UserDto> getUsers() {
        List<UserDto> us = new ArrayList<>();
        for (User user : users.values()) {
            us.add(userMapper.userToUserDto(user));
        }
        return us;
    }

    @Override
    public UserDto getUser(int id) {
        if (!users.containsKey(id)) throw new NotFoundException(HttpStatus.NOT_FOUND, "Такого юзера не существует");
        return userMapper.userToUserDto(users.get(id));
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        validEmail(userDto);
        User user = userMapper.userDtoToUser(userDto);
        users.put(user.getId(), user);
        return getUser(user.getId());
    }

    @Override
    public UserDto updateUser(UserDto userDto, int id) {
        if (!users.containsKey(id)) throw new NotFoundException(HttpStatus.NOT_FOUND, "Такого юзера не существует");
        userDto.setId(id);
        validEmail(userDto);
        User updatedUser = userMapper.updateUser(userDto, users.get(id));
        users.put(updatedUser.getId(), updatedUser);
        return getUser(id);

    }

    @Override
    public void deleteUser(int id) {
        users.remove(id);
    }

    private void validEmail(UserDto userDto) {
        for (User u : users.values()) {
            if (u.getEmail().equals(userDto.getEmail()) && u.getId() != userDto.getId())
                throw new ValidationException(HttpStatus.INTERNAL_SERVER_ERROR, "Такой мэйл уже есть");
        }
    }

    public void checkId(int itemId, int userId) {
        if (!users.containsKey(userId)) throw new NotFoundException(HttpStatus.NOT_FOUND, "Такого юзера не существует");
        if (!users.get(userId).getItemsList().contains(itemId))
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Такой вещи не существует");
    }

    @Override
    public void addItem(int itemId, int userId) {
        if (!users.containsKey(userId)) throw new NotFoundException(HttpStatus.NOT_FOUND, "Такого юзера не существует");
        users.get(userId).getItemsList().add(itemId);
    }
}

