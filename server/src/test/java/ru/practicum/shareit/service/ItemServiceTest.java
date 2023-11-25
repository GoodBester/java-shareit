package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemReturnDto;
import ru.practicum.shareit.item.service.impl.ItemServiceImpl;
import ru.practicum.shareit.user.model.DTO.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;


@Transactional
@SpringBootTest(
        properties = "db.name=test")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ItemServiceTest {

    private final ItemServiceImpl itemService;
    private final UserService userService;
    private final BookingServiceImpl bookingService;

    @Test
    public void createItemTest() {
        UserDto dto = new UserDto(1L, "name_test", "email@test.com");
        long id = userService.addUser(dto).getId();
        ItemDto itemDto = new ItemDto(1L, "name_test_item", "test_description", true, null, 0);
        long id2 = itemService.addItem(itemDto, id).getId();

        ItemReturnDto dto2 = itemService.getItem(id2, id);
        Assertions.assertEquals(itemDto.getName(), dto2.getName());
        Assertions.assertEquals(itemDto.getDescription(), dto2.getDescription());
    }

    @Test
    public void getItemTest() {
        UserDto dto = new UserDto(1L, "name_test", "email@test.com");
        long id = userService.addUser(dto).getId();
        ItemDto itemDto = new ItemDto(1L, "name_test_item", "test_description", true, null, 0);
        long id2 = itemService.addItem(itemDto, id).getId();
        ItemReturnDto dto2 = itemService.getItem(id2, id);
        Assertions.assertEquals(itemDto.getName(), dto2.getName());
        Assertions.assertEquals(itemDto.getDescription(), dto2.getDescription());

        final NotFoundException exception1 = Assertions.assertThrows(
                NotFoundException.class,
                () -> itemService.getItem(9999L, 999L));
    }

    @Test
    public void updateItemTest() {
        UserDto dto = new UserDto(1L, "name_test", "email@test.com");
        long id = userService.addUser(dto).getId();
        ItemDto itemDto = new ItemDto(1L, "name_test_item", "test_description", true, null, 0);
        long id2 = itemService.addItem(itemDto, id).getId();
        ItemReturnDto dto2 = itemService.getItem(id2, id);
        Assertions.assertEquals(itemDto.getName(), dto2.getName());
        Assertions.assertEquals(itemDto.getDescription(), dto2.getDescription());
        ItemDto itemDto1 = new ItemDto(1L, "name_test_item_update", "test_description_update", true, null, 0);

        final NotFoundException exception1 = Assertions.assertThrows(
                NotFoundException.class,
                () -> itemService.updateItem(9999L, 999L, itemDto1));
        ItemDto dto3 = itemService.updateItem(id2, id, itemDto1);
        Assertions.assertEquals(itemDto1.getName(), dto3.getName());
        Assertions.assertEquals(itemDto1.getDescription(), dto3.getDescription());
    }

    @Test
    public void searchItemTest() {
        UserDto dto = new UserDto(1L, "name_test", "email@test.com");
        long id = userService.addUser(dto).getId();
        ItemDto itemDto = new ItemDto(1L, "name_test_item", "test_description", true, null, 0);
        long id2 = itemService.addItem(itemDto, id).getId();
        Pageable page = Pageable.ofSize(1);

        ItemDto dto2 = itemService.search("name_test_item", page).get(0);
        Assertions.assertEquals(itemDto.getName(), dto2.getName());
        Assertions.assertEquals(itemDto.getDescription(), dto2.getDescription());

        Assertions.assertEquals(0, itemService.search("9999999", page).size());
    }

    @Test
    public void getUserItemTest() {
        UserDto dto = new UserDto(1L, "name_test", "email@test.com");
        long id = userService.addUser(dto).getId();
        ItemDto itemDto = new ItemDto(1L, "name_test_item", "test_description", true, null, 0);
        ItemDto itemDto1 = new ItemDto(9, "name_test_item1", "test_description3", true, null, 0);
        ItemDto itemDto2 = new ItemDto(1L, "name_test_item2", "test_description4", true, null, 0);

        long id2 = itemService.addItem(itemDto, id).getId();
        long id3 = itemService.addItem(itemDto1, id).getId();
        long id4 = itemService.addItem(itemDto2, id).getId();
        Pageable page = Pageable.ofSize(4);

        Assertions.assertNotNull(itemService.getUserItem(id, page));
        Assertions.assertEquals(3, itemService.getUserItem(id, page).size());
    }

    @Test
    public void addCommentItemTest() {
        UserDto dto = new UserDto(0, "name_test", "email@test.com");
        long id = userService.addUser(dto).getId();
        ItemDto itemDto = new ItemDto(1L, "name_test_item", "test_description", true, null, 0);
        long id2 = itemService.addItem(itemDto, id).getId();
        CommentDto commentDto = new CommentDto(0, "test_author_name", "text", LocalDateTime.now());
        final ValidationException exception1 = Assertions.assertThrows(
                ValidationException.class,
                () -> itemService.addComment(commentDto, id, id2));
        UserDto dto1 = new UserDto(0, "name_test22", "email.test@.com");
        long id1 = userService.addUser(dto1).getId();
        BookingDto bookingDto = new BookingDto(0L, id2, LocalDateTime.now().minusDays(11), LocalDateTime.now().minusDays(5), Status.APPROVED);
        long bookingId = bookingService.create(bookingDto, id1).getId();
        bookingService.setApproved(id, bookingId, true);
        itemService.addComment(commentDto, id1, id2);
        Assertions.assertEquals(commentDto.getAuthorName(), itemService.getItem(id2, id1).getComments().get(0).getAuthorName());
        Assertions.assertEquals(commentDto.getText(), itemService.getItem(id2, id1).getComments().get(0).getText());
    }
}
