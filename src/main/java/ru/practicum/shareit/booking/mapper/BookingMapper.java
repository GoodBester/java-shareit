package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

public class BookingMapper {
    public static Booking mapToDto(BookingDto bookingDto, int id) {
        Booking booking = new Booking();
        booking.setId(id);
        booking.setItemId(bookingDto.getItemId());
        booking.setFrom(bookingDto.getFrom());
        booking.setTo(bookingDto.getTo());
        booking.setStatus(bookingDto.isStatus());
        booking.setReview(bookingDto.getReview());
        return booking;
    }
}
