package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.DAO.CommentRepository;
import ru.practicum.shareit.item.service.impl.ItemServiceImpl;
import ru.practicum.shareit.request.DAO.ItemRequestRepository;
import ru.practicum.shareit.user.model.DTO.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;

@Transactional
@SpringBootTest(
        properties = "db.name=test")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceTest {

    private final UserService userService;


    @Test
    public void createTest() {
        UserDto dto = new UserDto(1L, "name_test", "email@test.com");
        long id = userService.addUser(dto).getId();
        UserDto dto2 = userService.getUser(id);
        Assertions.assertEquals(dto.getName(), dto2.getName());
        Assertions.assertEquals(dto.getEmail(), dto2.getEmail());
    }

    @Test
    public void getUserTest() {
        UserDto dto = new UserDto(111L, "name_test", "email@test.com");
        long id = userService.addUser(dto).getId();
        UserDto dto2 = userService.getUser(id);
        Assertions.assertEquals(dto.getName(), dto2.getName());
        Assertions.assertEquals(dto.getEmail(), dto2.getEmail());
        final NotFoundException exception1 = Assertions.assertThrows(
                NotFoundException.class,
                () -> userService.getUser(9999L));
    }

    @Test
    public void updateUserTest() {
        UserDto dto = new UserDto(111L, "name_test", "email@test.com");
        long id = userService.addUser(dto).getId();
        UserDto dto2 = userService.getUser(id);
        Assertions.assertEquals(dto.getName(), dto2.getName());
        Assertions.assertEquals(dto.getEmail(), dto2.getEmail());
        UserDto dto1 = new UserDto(111L, "name_test_update", "update_email@test.com");

        final NotFoundException exception1 = Assertions.assertThrows(
                NotFoundException.class,
                () -> userService.updateUser(dto1, 9999L));
        UserDto userDto3 = userService.updateUser(dto1, id);
        Assertions.assertEquals(dto1.getName(), userDto3.getName());
        Assertions.assertEquals(dto1.getEmail(), userDto3.getEmail());
    }

    @Test
    public void deleteUserTest() {
        UserDto dto = new UserDto(111L, "name_test", "email@test.com");
        long id = userService.addUser(dto).getId();
        UserDto dto2 = userService.getUser(id);
        Assertions.assertEquals(dto.getName(), dto2.getName());
        Assertions.assertEquals(dto.getEmail(), dto2.getEmail());
        userService.deleteUser(id);
        final NotFoundException exception1 = Assertions.assertThrows(
                NotFoundException.class,
                () -> userService.getUser(id));

    }
}
