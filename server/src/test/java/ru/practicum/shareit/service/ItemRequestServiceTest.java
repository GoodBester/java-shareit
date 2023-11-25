package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestReturnDto;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.model.DTO.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Transactional
@SpringBootTest(
        properties = "db.name=test")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ItemRequestServiceTest {

    private final UserServiceImpl userService;

    private final ItemRequestServiceImpl itemRequestService;

    @Test
    public void createRequestTest() {
        UserDto userDto1 = new UserDto(0, "name_test", "email@test.com");
        long userId1 = userService.addUser(userDto1).getId();
        LocalDateTime time = LocalDateTime.now();
        ItemRequestDto requestDto = new ItemRequestDto(0L, "test_description", time);
        long dtoId = itemRequestService.addRequest(userId1, requestDto).getId();
        ItemRequestReturnDto returnDto = itemRequestService.getRequest(userId1, dtoId);
        Assertions.assertEquals(requestDto.getCreated(), returnDto.getCreated());
        Assertions.assertEquals(requestDto.getDescription(), returnDto.getDescription());
    }

    @Test
    public void getRequestTest() {
        UserDto userDto1 = new UserDto(0, "name_test", "email@test.com");
        long userId1 = userService.addUser(userDto1).getId();
        LocalDateTime time = LocalDateTime.now();
        ItemRequestDto requestDto = new ItemRequestDto(0L, "test_description", time);
        long dtoId = itemRequestService.addRequest(userId1, requestDto).getId();
        ItemRequestReturnDto returnDto = itemRequestService.getRequest(userId1, dtoId);
        Assertions.assertEquals(requestDto.getCreated(), returnDto.getCreated());
        Assertions.assertEquals(requestDto.getDescription(), returnDto.getDescription());
        final NotFoundException exception1 = Assertions.assertThrows(
                NotFoundException.class,
                () -> itemRequestService.getRequest(userId1, 9999L));
    }

    @Test
    public void getUserRequestTest() {
        UserDto userDto1 = new UserDto(0, "name_test", "email@test.com");
        long userId1 = userService.addUser(userDto1).getId();
        LocalDateTime time = LocalDateTime.now();
        ItemRequestDto requestDto = new ItemRequestDto(0L, "test_description", time);
        Assertions.assertEquals(0, itemRequestService.getRequests(userId1).size());
        itemRequestService.addRequest(userId1, requestDto);
        itemRequestService.addRequest(userId1, requestDto);
        itemRequestService.addRequest(userId1, requestDto);
        Assertions.assertNotNull(itemRequestService.getRequests(userId1));
        Assertions.assertEquals(3, itemRequestService.getRequests(userId1).size());

    }

    @Test
    public void getAllRequestTest() {
        Pageable page = Pageable.ofSize(5);
        UserDto userDto1 = new UserDto(0, "name_test", "email@test.com");
        long userId1 = userService.addUser(userDto1).getId();
        LocalDateTime time = LocalDateTime.now();
        ItemRequestDto requestDto = new ItemRequestDto(0L, "test_description", time);
        itemRequestService.addRequest(userId1, requestDto);
        itemRequestService.addRequest(userId1, requestDto);
        itemRequestService.addRequest(userId1, requestDto);
        Assertions.assertEquals(0, itemRequestService.getAllRequests(userId1, page).size());
        UserDto userDto2 = new UserDto(0, "name_test1", "email@test1.com");
        long userId2 = userService.addUser(userDto2).getId();
        ItemRequestDto requestDto1 = new ItemRequestDto(0L, "test_description1", time);
        itemRequestService.addRequest(userId2, requestDto1);
        Assertions.assertEquals(3, itemRequestService.getAllRequests(userId2, page).size());
        Assertions.assertEquals(1, itemRequestService.getAllRequests(userId1, page).size());

    }
}
