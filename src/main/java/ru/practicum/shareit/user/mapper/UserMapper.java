package ru.practicum.shareit.user.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.DTO.UserDto;
import ru.practicum.shareit.user.model.User;

@Component
public class UserMapper {

    private int idSetter = 1;

    public User userDtoToUser(UserDto userDto) {
        User user = new User();
        user.setId(idSetter++);
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        return user;
    }

    public UserDto userToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    public User updateUser(UserDto userDto, User user) {
        if (userDto.getEmail() != null) user.setEmail(userDto.getEmail());
        if (userDto.getName() != null) user.setName(userDto.getName());
        return user;
    }


}
