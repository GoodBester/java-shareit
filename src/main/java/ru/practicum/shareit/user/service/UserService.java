package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.DAO.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        return userRepository.getUsers();
    }

    public User getUser(int id) {
        return userRepository.getUser(id);
    }

    public User addUser(User user) {
        return userRepository.addUser(user);
    }

    public User updateUser(User user, int id) {
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
