package ru.practicum.shareit.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exceptions.IncorrectStateException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.impl.ItemServiceImpl;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
public class BookingServiceUnitTest {

    @InjectMocks
    BookingServiceImpl bookingService;
    BookingDto bookingDto;
    Booking booking;
    Item item;
    User user;
    ItemRequest itemRequest;

    @Mock
    ItemServiceImpl itemService;
    @Mock
    UserServiceImpl userService;
    @Mock
    BookingRepository mockBookingRepository;
    @Mock
    ModelMapper mapper;


    @BeforeEach
    void setUp() {
        LocalDateTime time = LocalDateTime.now();
        bookingDto = new BookingDto(1L, 1L, time, time.plusDays(11), Status.APPROVED);
        user = new User(10L, "name", "email");
        itemRequest = new ItemRequest(11L, "description", user, LocalDateTime.now());
        item = new Item(1L, "name", "description", true, user, itemRequest);
        booking = new Booking(1L, item, time, time.plusDays(1), user, Status.APPROVED);

    }


    @Test
    public void createFailTimeTest() {
        Mockito
                .when(itemService.getItemEntity(anyLong()))
                .thenReturn(null);
        Mockito
                .when(userService.getUserEntity(anyLong()))
                .thenReturn(null);
        BookingDto bookingDto1 = bookingDto;
        bookingDto1.setEnd(bookingDto.getStart().minusMinutes(100));
        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> bookingService.create(bookingDto1, 10L));
        Assertions.assertEquals("400 BAD_REQUEST \"Incorrect booking time\"", exception.getMessage());
    }

    @Test
    public void createFailAvailableTest() {
        item.setAvailable(false);
        Mockito
                .when(itemService.getItemEntity(anyLong()))
                .thenReturn(item);
        final ValidationException exception1 = Assertions.assertThrows(
                ValidationException.class,
                () -> bookingService.create(bookingDto, 10L));
        Assertions.assertEquals("400 BAD_REQUEST \"Item is not available\"", exception1.getMessage());
    }

    @Test
    public void createFailOwnerTest() {
        Mockito
                .when(userService.getUserEntity(anyLong()))
                .thenReturn(user);
        Mockito
                .when(itemService.getItemEntity(anyLong()))
                .thenReturn(item);
        final NotFoundException exception1 = Assertions.assertThrows(
                NotFoundException.class,
                () -> bookingService.create(bookingDto, 10L));
        Assertions.assertEquals("404 NOT_FOUND \"You are owner\"", exception1.getMessage());
    }

    @Test
    public void getAllBookingsAllStateOwnerTest() {
        List<Booking> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            list.add(booking);
        }
        Pageable page = Pageable.ofSize(1);
        Mockito
                .when(userService.getUserEntity(anyLong()))
                .thenReturn(user);
        Mockito
                .when(mockBookingRepository.findAllBookingByItem_Owner_IdOrderByStartDesc(anyLong(), any()))
                .thenReturn(list);
        Assertions.assertNotNull(bookingService.getAllBookingFromUser(1L, "ALL", "owner", page));
        Assertions.assertEquals(3, bookingService.getAllBookingFromUser(1L, "ALL", "owner", page).size());
    }

    @Test
    public void getAllBookingsAllStateUserTest() {
        List<Booking> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            list.add(booking);
        }
        Pageable page = Pageable.ofSize(1);
        Mockito
                .when(userService.getUserEntity(anyLong()))
                .thenReturn(user);
        Mockito
                .when(mockBookingRepository.findAllByBooker_IdOrderByStartDesc(anyLong(), any()))
                .thenReturn(list);
        Assertions.assertNotNull(bookingService.getAllBookingFromUser(1L, "ALL", "user", page));
        Assertions.assertEquals(3, bookingService.getAllBookingFromUser(1L, "ALL", "user", page).size());
    }

    @Test
    public void getAllBookingsFutureStateOwnerTest() {
        List<Booking> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            list.add(booking);
        }
        Pageable page = Pageable.ofSize(1);
        Mockito
                .when(userService.getUserEntity(anyLong()))
                .thenReturn(user);
        Mockito
                .when(mockBookingRepository.findAllBookingByItem_Owner_IdAndStartAfterOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(list);
        Assertions.assertNotNull(bookingService.getAllBookingFromUser(1L, "FUTURE", "owner", page));
        Assertions.assertEquals(3, bookingService.getAllBookingFromUser(1L, "FUTURE", "owner", page).size());
    }

    @Test
    public void getAllBookingsFutureStateUserTest() {
        List<Booking> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            list.add(booking);
        }
        Pageable page = Pageable.ofSize(1);
        Mockito
                .when(userService.getUserEntity(anyLong()))
                .thenReturn(user);
        Mockito
                .when(mockBookingRepository.findAllBookingByBookerIdAndStartAfterOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(list);
        Assertions.assertNotNull(bookingService.getAllBookingFromUser(1L, "FUTURE", "rr", page));
        Assertions.assertEquals(3, bookingService.getAllBookingFromUser(1L, "FUTURE", "rr", page).size());
    }

    @Test
    public void getAllBookingsPastStateOwnerTest() {
        List<Booking> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            list.add(booking);
        }
        Pageable page = Pageable.ofSize(1);
        Mockito
                .when(userService.getUserEntity(anyLong()))
                .thenReturn(user);
        Mockito
                .when(mockBookingRepository.findAllBookingByItem_Owner_IdAndEndBeforeOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(list);
        Assertions.assertNotNull(bookingService.getAllBookingFromUser(1L, "PAST", "owner", page));
        Assertions.assertEquals(3, bookingService.getAllBookingFromUser(1L, "PAST", "owner", page).size());
    }

    @Test
    public void getAllBookingsPastStateUserTest() {
        List<Booking> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            list.add(booking);
        }
        Pageable page = Pageable.ofSize(1);
        Mockito
                .when(userService.getUserEntity(anyLong()))
                .thenReturn(user);
        Mockito
                .when(mockBookingRepository.findAllBookingByBookerIdAndEndBeforeOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(list);
        Assertions.assertNotNull(bookingService.getAllBookingFromUser(1L, "PAST", "rr", page));
        Assertions.assertEquals(3, bookingService.getAllBookingFromUser(1L, "PAST", "rr", page).size());
    }

    @Test
    public void getAllBookingsCurrentStateOwnerTest() {
        List<Booking> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            list.add(booking);
        }
        Pageable page = Pageable.ofSize(1);
        Mockito
                .when(userService.getUserEntity(anyLong()))
                .thenReturn(user);
        Mockito
                .when(mockBookingRepository.findAllBookingByItem_Owner_IdAndStartBeforeAndEndAfterOrderByStartDesc(anyLong(), any(), any(), any()))
                .thenReturn(list);
        Assertions.assertNotNull(bookingService.getAllBookingFromUser(1L, "CURRENT", "owner", page));
        Assertions.assertEquals(3, bookingService.getAllBookingFromUser(1L, "CURRENT", "owner", page).size());
    }

    @Test
    public void getAllBookingsCurrentStateUserTest() {
        List<Booking> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            list.add(booking);
        }
        Pageable page = Pageable.ofSize(1);
        Mockito
                .when(userService.getUserEntity(anyLong()))
                .thenReturn(user);
        Mockito
                .when(mockBookingRepository.findAllBookingByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(anyLong(), any(), any(), any()))
                .thenReturn(list);
        Assertions.assertNotNull(bookingService.getAllBookingFromUser(1L, "CURRENT", "rr", page));
        Assertions.assertEquals(3, bookingService.getAllBookingFromUser(1L, "CURRENT", "rr", page).size());
    }

    @Test
    public void getAllBookingsWaitingStateUserTest() {
        List<Booking> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            list.add(booking);
        }
        Pageable page = Pageable.ofSize(1);
        Mockito
                .when(userService.getUserEntity(anyLong()))
                .thenReturn(user);
        Mockito
                .when(mockBookingRepository.findAllBookingByBookerIdAndStatusOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(list);
        Assertions.assertNotNull(bookingService.getAllBookingFromUser(1L, "WAITING", "rr", page));
        Assertions.assertEquals(3, bookingService.getAllBookingFromUser(1L, "WAITING", "rr", page).size());
    }

    @Test
    public void getAllBookingsWaitingStateOwnerTest() {
        List<Booking> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            list.add(booking);
        }
        Pageable page = Pageable.ofSize(1);
        Mockito
                .when(userService.getUserEntity(anyLong()))
                .thenReturn(user);
        Mockito
                .when(mockBookingRepository.findAllBookingByItem_Owner_IdAndStatusOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(list);
        Assertions.assertNotNull(bookingService.getAllBookingFromUser(1L, "WAITING", "owner", page));
        Assertions.assertEquals(3, bookingService.getAllBookingFromUser(1L, "WAITING", "owner", page).size());
    }

    @Test
    public void getAllBookingsRejectedStateOwnerTest() {
        List<Booking> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            list.add(booking);
        }
        Pageable page = Pageable.ofSize(1);
        Mockito
                .when(userService.getUserEntity(anyLong()))
                .thenReturn(user);
        Mockito
                .when(mockBookingRepository.findAllBookingByItem_Owner_IdAndStatusOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(list);
        Assertions.assertNotNull(bookingService.getAllBookingFromUser(1L, "REJECTED", "owner", page));
        Assertions.assertEquals(3, bookingService.getAllBookingFromUser(1L, "REJECTED", "owner", page).size());
    }

    @Test
    public void getAllBookingsRejectedStateUserTest() {
        List<Booking> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            list.add(booking);
        }
        Pageable page = Pageable.ofSize(1);
        Mockito
                .when(userService.getUserEntity(anyLong()))
                .thenReturn(user);
        Mockito
                .when(mockBookingRepository.findAllBookingByBookerIdAndStatusOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(list);
        Assertions.assertNotNull(bookingService.getAllBookingFromUser(1L, "REJECTED", "RR", page));
        Assertions.assertEquals(3, bookingService.getAllBookingFromUser(1L, "REJECTED", "rr", page).size());
    }

    @Test
    public void getAllBookingsIncorrectStateTest() {
        List<Booking> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            list.add(booking);
        }
        Pageable page = Pageable.ofSize(1);
        Mockito
                .when(userService.getUserEntity(anyLong()))
                .thenReturn(user);
        final IncorrectStateException exception1 = Assertions.assertThrows(
                IncorrectStateException.class,
                () -> bookingService.getAllBookingFromUser(1L, "rr", "rr", page));
    }

    @Test
    public void getAllBookingsEmptyTest() {
        Pageable page = Pageable.ofSize(1);
        Mockito
                .when(userService.getUserEntity(anyLong()))
                .thenReturn(user);
        final NotFoundException exception1 = Assertions.assertThrows(
                NotFoundException.class,
                () -> bookingService.getAllBookingFromUser(1L, "ALL", "owner", page));
        Assertions.assertEquals("404 NOT_FOUND \"This user has not items\"", exception1.getMessage());
    }

}
