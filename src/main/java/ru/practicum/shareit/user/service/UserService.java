package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.DAO.UserRepository;
import ru.practicum.shareit.user.model.DTO.UserDto;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserService {
    private final UserRepository userRepository;


    public List<UserDto> getUsers() {
        return userRepository.getUsers();
    }

    public UserDto getUser(int id) {
        return userRepository.getUser(id);
    }

    public UserDto addUser(UserDto user) {
        return userRepository.addUser(user);
    }

    public UserDto updateUser(UserDto user, int id) {
        return userRepository.updateUser(user, id);
    }

    public void deleteUser(int id) {
        userRepository.deleteUser(id);
    }

    public void checkId(int itemId, int userId) {
        userRepository.checkId(itemId, userId);
    }

    public void addItem(int itemId, int userId) {
        userRepository.addItem(itemId, userId);
    }
}
