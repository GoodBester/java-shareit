package ru.practicum.shareit.user.DAO;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {

    List<User> getUsers();

    User getUser(int id);

    User addUser(User user);

    User updateUser(User user, int id);

    void deleteUser(int id);

    void checkId(int itemId, int userId);

    void addItem(int itemId, int userId);
}
