package ru.practicum.shareit.item.DAO.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.DAO.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryItemRepository implements ItemRepository {

    private UserService userService;
    private static int idSetter = 1;

    HashMap<Integer, Item> items = new HashMap<>();

    @Autowired
    public InMemoryItemRepository(UserService userService) {
        this.userService = userService;
    }

    @Override
    public List<ItemDto> getUserItem(int id) {
        List<ItemDto> userItem = new ArrayList<>();
        for (Item i : items.values()) {
            if (i.getOwner() == id) userItem.add(ItemMapper.itemToItemDto(i));
        }
        return userItem;
    }

    @Override
    public ItemDto getItem(int id) {
        return ItemMapper.itemToItemDto(items.get(id));
    }

    @Override
    public ItemDto addItem(ItemDto itemDto, int id) {
        Item item = ItemMapper.itemDtoToItem(itemDto, id);
        if (userService.getUser(id) == null)
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Такого юзера не существует");
        item.setId(idSetter++);
        userService.addItem(item.getId(), id);
        items.put(item.getId(), item);
        return getItem(item.getId());
    }

    @Override
    public List<ItemDto> search(String text) {
        List<ItemDto> userItem = new ArrayList<>();
        if (text.isEmpty()) return userItem;
        for (Item i : items.values()) {
            if (i.getName().toLowerCase().contains(text.toLowerCase()) ||
                    i.getDescription().toLowerCase().contains(text.toLowerCase()) &&
                            i.isAvailable()) userItem.add(ItemMapper.itemToItemDto(i));
        }
        return userItem;
    }

    @Override
    public ItemDto updateItem(int itemId, int id, ItemDto itemDto) {
        userService.checkId(itemId, id);
        Item item = ItemMapper.updateItem(itemDto, id, items.get(itemId));
        item.setId(itemId);
        items.put(itemId, item);
        return ItemMapper.itemToItemDto(items.get(itemId));
    }
}
