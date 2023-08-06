package ru.practicum.shareit.item.mapper;

import org.springframework.http.HttpStatus;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {

    public static Item itemDtoToItem(ItemDto itemDto, int id) {
        if (itemDto.getAvailable() == null) throw new ValidationException(HttpStatus.BAD_REQUEST, "Available not");
        Item item = new Item();
        item.setOwner(id);
        item.setName(itemDto.getName());
        item.setAvailable(itemDto.getAvailable());
        item.setDescription(itemDto.getDescription());
        item.setCountRented(itemDto.getCountRented());
        return item;
    }

    public static ItemDto itemToItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setAvailable(item.isAvailable());
        itemDto.setDescription(item.getDescription());
        itemDto.setCountRented(item.getCountRented());
        return itemDto;
    }

    public static Item updateItem(ItemDto itemDto, int id, Item item) {
        item.setOwner(id);
        if (itemDto.getName() != null) item.setName(itemDto.getName());
        if (itemDto.getAvailable() != null) item.setAvailable(itemDto.getAvailable());
        if (itemDto.getDescription() != null) item.setDescription(itemDto.getDescription());
//        if(itemDto.getCountRented() != null) item.setCountRented(itemDto.getCountRented());
        return item;
    }
}
