package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.booking.dto.BookingReturnDtoForItem;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class ItemReturnDto {

    private long id;
    private String name;
    private String description;
    private boolean available;
    private User owner;
    private int requestId;
    private BookingReturnDtoForItem lastBooking;
    private BookingReturnDtoForItem nextBooking;
    private List<CommentDto> comments;
}
