package ru.practicum.shareit.item.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.service.UserService;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemMapper {
    private final UserService userService;

    private int idSetter = 1;


    public Item itemDtoToItem(ItemDto itemDto, int id) {
        if (itemDto.getAvailable() == null) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Available not");
        }
        if (userService.getUser(id) == null) {
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Такого юзера не существует");
        }
        Item item = new Item();
        item.setId(idSetter++);
        item.setOwner(id);
        item.setName(itemDto.getName());
        item.setAvailable(itemDto.getAvailable());
        item.setDescription(itemDto.getDescription());
        item.setCountRented(itemDto.getCountRented());
        userService.addItem(item.getId(), id);
        return item;
    }

    public ItemDto itemToItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setAvailable(item.isAvailable());
        itemDto.setDescription(item.getDescription());
        itemDto.setCountRented(item.getCountRented());
        return itemDto;
    }

    public Item updateItem(ItemDto itemDto, int id, Item item) {
        userService.checkId(item.getId(), id);
        if (itemDto.getName() != null) item.setName(itemDto.getName());
        if (itemDto.getAvailable() != null) item.setAvailable(itemDto.getAvailable());
        if (itemDto.getDescription() != null) item.setDescription(itemDto.getDescription());
        return item;
    }
}
