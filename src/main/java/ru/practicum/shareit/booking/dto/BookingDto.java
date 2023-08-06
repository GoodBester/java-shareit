package ru.practicum.shareit.booking.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class BookingDto {
    private int itemId;
    private LocalDateTime from;
    private LocalDateTime to;
    private boolean status;
    private String review;
}
