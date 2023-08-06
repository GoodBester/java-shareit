package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

public class RequestMapper {
    public static ItemRequest mapToDto(ItemRequestDto itemRequestDto) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setRequestedItem(itemRequestDto.getRequestedItem());
        itemRequest.setOfferedItems(itemRequestDto.getOfferedItems());
        return itemRequest;
    }
}
