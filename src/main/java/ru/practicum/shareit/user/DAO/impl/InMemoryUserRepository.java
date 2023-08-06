package ru.practicum.shareit.user.DAO.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.DAO.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryUserRepository implements UserRepository {

    private HashMap<Integer, User> users = new HashMap<>();
    private int id = 1;

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUser(int id) {
        return users.get(id);
    }

    @Override
    public User addUser(User user) {
        validEmail(user);
        user.setId(id++);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user, int id) {
        if (!users.containsKey(id)) throw new NotFoundException(HttpStatus.NOT_FOUND, "Такого юзера не существует");
        user.setId(id);
        validEmail(user);
        User u = users.get(id);
        if (user.getEmail() != null) u.setEmail(user.getEmail());
        if (user.getName() != null) u.setName(user.getName());
        u.setId(id);
        users.put(u.getId(), u);
        return users.get(id);

    }

    @Override
    public void deleteUser(int id) {
        users.remove(id);
    }

    private void validEmail(User user) {
        for (User u : users.values()) {
            if (u.getEmail().equals(user.getEmail()) && u.getId() != user.getId())
                throw new ValidationException(HttpStatus.INTERNAL_SERVER_ERROR, "Такой мэйл уже есть");
        }
    }

    public void checkId(int itemId, int userId) {
        if (!users.containsKey(userId)) throw new NotFoundException(HttpStatus.NOT_FOUND, "Такого юзера не существует");
        if (!getUser(userId).getItemsList().contains(itemId))
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Такой вещи не существует");
        ;
    }

    @Override
    public void addItem(int itemId, int userId) {
        if (!users.containsKey(userId)) throw new NotFoundException(HttpStatus.NOT_FOUND, "Такого юзера не существует");
        getUser(userId).getItemsList().add(itemId);
    }
}

