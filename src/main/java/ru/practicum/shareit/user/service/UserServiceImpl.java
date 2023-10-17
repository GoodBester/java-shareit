package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.DAO.UserRepository;
import ru.practicum.shareit.user.model.DTO.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper mapper;


    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public UserDto getUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "User not found."));
        return mapToDto(user);
    }

    @Override
    public User getUserEntity(long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "User not found."));
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        return mapToDto(userRepository.save(mapToUser(userDto)));
    }

    @Override
    public UserDto updateUser(UserDto userDto, long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "User not found."));
        if (userDto.getName() != null) user.setName(userDto.getName());
        if (userDto.getEmail() != null) user.setEmail(userDto.getEmail());
        return mapToDto(userRepository.save(user));
    }

    @Override
    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }


    private UserDto mapToDto(User user) {
        return mapper.map(user, UserDto.class);
    }

    private User mapToUser(UserDto userDto) {
        return mapper.map(userDto, User.class);
    }
}
