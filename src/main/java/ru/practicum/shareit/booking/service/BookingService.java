package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingReturnDto;

import java.util.List;

public interface BookingService {

    BookingReturnDto create(BookingDto dto, Long bookerId);

    BookingReturnDto setApproved(Long userId, Long bookingId, Boolean approved);

    BookingReturnDto getBooking(Long userId, Long bookingId);

    List<BookingReturnDto> getAllBookingFromUser(Long userId, String state, String userType);

}
