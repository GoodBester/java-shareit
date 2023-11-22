package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingReturnDto;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exceptions.IncorrectStateException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.impl.ItemServiceImpl;
import ru.practicum.shareit.user.model.DTO.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Transactional
@SpringBootTest(
        properties = "db.name=root")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookingServiceTest {

    private final ItemServiceImpl itemService;
    private final UserService userService;
    private final BookingServiceImpl bookingService;


    @Test
    public void createBookingTest() {
        UserDto userDto1 = new UserDto(0, "name_test", "email@test.com");
        long userId1 = userService.addUser(userDto1).getId();
        ItemDto itemDto = new ItemDto(0, "name_test_item", "test_description", true, null, 0);
        long itemId1 = itemService.addItem(itemDto, userId1).getId();
        BookingDto bookingDto = new BookingDto(0L, itemId1, LocalDateTime.now().minusDays(11), LocalDateTime.now().minusDays(5), Status.APPROVED);
        UserDto userDto2 = new UserDto(0, "name_test", "emailtest2@test.com");
        long userId2 = userService.addUser(userDto2).getId();
        BookingReturnDto bookingReturnDto = bookingService.create(bookingDto, userId2);
        Assertions.assertEquals(bookingDto.getItemId(), bookingReturnDto.getItem().getId());
        Assertions.assertEquals(userId2, bookingReturnDto.getBooker().getId());
        Assertions.assertEquals(Status.WAITING, bookingReturnDto.getStatus());
    }

    @Test
    public void setApprovedBookingTest() {
        UserDto userDto1 = new UserDto(0, "name_test", "email@test.com");
        long userId1 = userService.addUser(userDto1).getId();
        ItemDto itemDto = new ItemDto(0, "name_test_item", "test_description", true, null, 0);
        long itemId1 = itemService.addItem(itemDto, userId1).getId();
        BookingDto bookingDto = new BookingDto(0L, itemId1, LocalDateTime.now().minusDays(11), LocalDateTime.now().minusDays(5), Status.APPROVED);
        UserDto userDto2 = new UserDto(0, "name_test", "emailtest2@test.com");
        long userId2 = userService.addUser(userDto2).getId();
        BookingReturnDto bookingReturnDto = bookingService.create(bookingDto, userId2);
        Assertions.assertEquals(Status.WAITING, bookingService.getBooking(userId1, bookingReturnDto.getId()).getStatus());

        final NotFoundException exception1 = Assertions.assertThrows(
                NotFoundException.class,
                () -> bookingService.setApproved(userId1, 99999L, false));

        final NotFoundException exception2 = Assertions.assertThrows(
                NotFoundException.class,
                () -> bookingService.setApproved(9999L, bookingReturnDto.getId(), false));

        bookingService.setApproved(userId1, bookingReturnDto.getId(), false);
        Assertions.assertEquals(Status.REJECTED, bookingService.getBooking(userId1, bookingReturnDto.getId()).getStatus());

        bookingService.setApproved(userId1, bookingReturnDto.getId(), true);
        Assertions.assertEquals(Status.APPROVED, bookingService.getBooking(userId1, bookingReturnDto.getId()).getStatus());

        final ValidationException exception3 = Assertions.assertThrows(
                ValidationException.class,
                () -> bookingService.setApproved(userId1, bookingReturnDto.getId(), true));
    }

    @Test
    public void getBookingTest() {
        UserDto userDto1 = new UserDto(0, "name_test", "email@test.com");
        long userId1 = userService.addUser(userDto1).getId();
        ItemDto itemDto = new ItemDto(0, "name_test_item", "test_description", true, null, 0);
        long itemId1 = itemService.addItem(itemDto, userId1).getId();
        BookingDto bookingDto = new BookingDto(0L, itemId1, LocalDateTime.now().minusDays(11), LocalDateTime.now().minusDays(5), Status.APPROVED);
        UserDto userDto2 = new UserDto(0, "name_test", "emailtest2@test.com");
        long userId2 = userService.addUser(userDto2).getId();
        BookingReturnDto bookingReturnDto = bookingService.create(bookingDto, userId2);

        final NotFoundException exception1 = Assertions.assertThrows(
                NotFoundException.class,
                () -> bookingService.getBooking(userId1, 99999L));

        final NotFoundException exception2 = Assertions.assertThrows(
                NotFoundException.class,
                () -> bookingService.getBooking(99999L, bookingReturnDto.getId()));

        Assertions.assertEquals(bookingDto.getItemId(), bookingReturnDto.getItem().getId());
        Assertions.assertEquals(userId1, bookingReturnDto.getItem().getOwner().getId());
    }

    @Test
    public void getAllBookingTest() {
        UserDto userDto1 = new UserDto(0, "name_test", "email@test.com");
        long userId1 = userService.addUser(userDto1).getId();
        ItemDto itemDto = new ItemDto(0, "name_test_item", "test_description", true, null, 0);
        long itemId1 = itemService.addItem(itemDto, userId1).getId();
        BookingDto bookingDto = new BookingDto(0L, itemId1, LocalDateTime.now().minusDays(11), LocalDateTime.now().minusDays(5), Status.APPROVED);
        UserDto userDto2 = new UserDto(0, "name_test", "emailtest2@test.com");
        long userId2 = userService.addUser(userDto2).getId();
        BookingReturnDto bookingReturnDto = bookingService.create(bookingDto, userId2);
        ItemDto itemDto1 = new ItemDto(0, "name_test_item1", "test_description1", true, null, 0);
        long itemId2 = itemService.addItem(itemDto1, userId2).getId();
        BookingDto bookingDto1 = new BookingDto(0L, itemId2, LocalDateTime.now().minusDays(11), LocalDateTime.now().minusDays(5), Status.APPROVED);
        BookingReturnDto bookingReturnDto1 = bookingService.create(bookingDto1, userId1);
        Pageable page = Pageable.ofSize(4);

        Assertions.assertEquals(bookingDto.getItemId(), bookingService.getAllBookingFromUser(userId1, "WAITING", "owner", page).get(0).getItem().getId());
        Assertions.assertEquals(bookingDto1.getItemId(), bookingService.getAllBookingFromUser(userId1, "WAITING", "rrr", page).get(0).getItem().getId());

        final NotFoundException exception1 = Assertions.assertThrows(
                NotFoundException.class,
                () -> bookingService.getAllBookingFromUser(99999L, "WAITING", "rrr", page).get(0).getItem().getId());

        final IncorrectStateException exception2 = Assertions.assertThrows(
                IncorrectStateException.class,
                () -> bookingService.getAllBookingFromUser(userId1, "rrrrr", "rrr", page).get(0).getItem().getId());

    }
}
