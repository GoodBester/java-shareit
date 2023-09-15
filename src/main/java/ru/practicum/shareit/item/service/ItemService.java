package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    List<ItemDto> getUserItem(int id);

    ItemDto getItem(int id);

    ItemDto addItem(ItemDto itemDto, int id);

    List<ItemDto> search(String text);

    ItemDto updateItem(int itemId, int id, ItemDto itemDto);

}
