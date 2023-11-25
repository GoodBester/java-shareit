package ru.practicum.shareit.item.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemReturnDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    List<ItemReturnDto> getUserItem(long id, Pageable page);

    ItemReturnDto getItem(long id, long userId);

    ItemDto addItem(ItemDto itemDto, long id);

    CommentDto addComment(CommentDto commentDto, long userId, long itemId);

    List<ItemDto> search(String text, Pageable page);

    ItemDto updateItem(long itemId, long id, ItemDto itemDto);

    Item getItemEntity(long id);

    List<Item> findAllByRequest_Id(Long requestId);

}
