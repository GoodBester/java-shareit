package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookingDto {
    private Long id;
    private Long itemId;
    private LocalDateTime start;
    private LocalDateTime end;
    private Status status;
}