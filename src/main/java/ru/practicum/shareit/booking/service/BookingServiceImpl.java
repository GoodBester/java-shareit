package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingReturnDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.IncorrectStateException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;
    private final ModelMapper mapper;

    @Override
    @Transactional
    public BookingReturnDto create(BookingDto dto, Long bookerId) {
        Item item = itemService.getItemEntity(dto.getItemId());
        User booker = userService.getUserEntity(bookerId);
        if (dto.getEnd().isBefore(dto.getStart()) || dto.getStart().equals(dto.getEnd())) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Incorrect booking time");
        }
        if (!item.isAvailable()) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Item is not available");
        }
        if (booker.getId() == item.getOwner().getId()) {
            throw new NotFoundException(HttpStatus.NOT_FOUND, "You are owner");
        }
        Booking booking = mapToEntity(dto);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(Status.WAITING);
        return mapToReturnDto(bookingRepository.save(booking));
    }

    @Override
    public BookingReturnDto setApproved(Long userId, Long bookingId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "Booking not found."));
        if (booking.getItem().getOwner().getId() != userId) {
            throw new NotFoundException(HttpStatus.BAD_REQUEST, "You are not owner");
        }
        if (booking.getStatus().equals(Status.APPROVED)) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Status is already approved");
        }

        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        return mapToReturnDto(bookingRepository.save(booking));
    }

    @Override
    public BookingReturnDto getBooking(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "Booking not found."));
        User user = userService.getUserEntity(userId);
        if (!booking.getBooker().equals(user) && !booking.getItem().getOwner().equals(user)) {
            throw new NotFoundException(HttpStatus.BAD_REQUEST, "You should be a owner or booker of this item to get it");
        }
        return mapToReturnDto(booking);
    }

    @Override
    public List<BookingReturnDto> getAllBookingFromUser(Long userId, String state, String userType) {
        User user = userService.getUserEntity(userId);
        LocalDateTime now = LocalDateTime.now();
        boolean isOwner = userType.equals("owner");
        List<Booking> bookings;
        if (state == null) {
            state = "ALL";
        }
        switch (state) {
            case "ALL":
                if (isOwner) {
                    bookings = bookingRepository.findAllBookingByItem_Owner_IdOrderByStartDesc(user.getId());
                } else {
                    bookings = bookingRepository.findAllByBooker_IdOrderByStartDesc(user.getId());
                }
                break;

            case "FUTURE":
                if (isOwner) {
                    bookings = bookingRepository.findAllBookingByItem_Owner_IdAndStartAfterOrderByStartDesc(user.getId(), now);
                } else {
                    bookings = bookingRepository.findAllBookingByBookerIdAndStartAfterOrderByStartDesc(user.getId(), now);
                }
                break;

            case "PAST":
                if (isOwner) {
                    bookings = bookingRepository.findAllBookingByItem_Owner_IdAndEndBeforeOrderByStartDesc(user.getId(), now);
                } else {
                    bookings = bookingRepository.findAllBookingByBookerIdAndEndBeforeOrderByStartDesc(user.getId(), now);
                }
                break;

            case "CURRENT":
                if (isOwner) {
                    bookings = bookingRepository.findAllBookingByItem_Owner_IdAndStartBeforeAndEndAfterOrderByStartDesc(user.getId(), now, now);
                } else {
                    bookings = bookingRepository.findAllBookingByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(user.getId(), now, now);
                }
                break;

            case "WAITING":
                if (isOwner) {
                    bookings = bookingRepository.findAllBookingByItem_Owner_IdAndStatusOrderByStartDesc(user.getId(), Status.WAITING);
                } else {
                    bookings = bookingRepository.findAllBookingByBookerIdAndStatusOrderByStartDesc(user.getId(), Status.WAITING);
                }
                break;

            case "REJECTED":
                if (isOwner) {
                    bookings = bookingRepository.findAllBookingByItem_Owner_IdAndStatusOrderByStartDesc(user.getId(), Status.REJECTED);
                } else {
                    bookings = bookingRepository.findAllBookingByBookerIdAndStatusOrderByStartDesc(user.getId(), Status.REJECTED);
                }
                break;

            default:
                throw new IncorrectStateException();
        }
        if (bookings.isEmpty() && isOwner) {
            throw new NotFoundException(HttpStatus.NOT_FOUND, "This user has not items");
        }
        return bookings.stream().map(this::mapToReturnDto).collect(Collectors.toList());
    }


    private Booking mapToEntity(BookingDto bookingDto) {
        return mapper.map(bookingDto, Booking.class);
    }

    private BookingReturnDto mapToReturnDto(Booking booking) {
        return mapper.map(booking, BookingReturnDto.class);
    }
}
