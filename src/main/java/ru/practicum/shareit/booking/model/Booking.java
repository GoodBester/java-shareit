package ru.practicum.shareit.booking.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class Booking {
    private int id;
    private int itemId;
    private LocalDateTime from;
    private LocalDateTime to;
    private boolean status;
    private String review;

}
