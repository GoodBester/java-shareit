package ru.practicum.shareit.item.DAO.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.DAO.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryItemRepository implements ItemRepository {

    private ItemMapper itemMapper;

    HashMap<Integer, Item> items = new HashMap<>();

    @Autowired
    public InMemoryItemRepository(ItemMapper itemMapper) {
        this.itemMapper = itemMapper;
    }

    @Override
    public List<ItemDto> getUserItem(int id) {
        List<ItemDto> userItem = new ArrayList<>();
        for (Item i : items.values()) {
            if (i.getOwner() == id) userItem.add(itemMapper.itemToItemDto(i));
        }
        return userItem;
    }

    @Override
    public ItemDto getItem(int id) {
        return itemMapper.itemToItemDto(items.get(id));
    }

    @Override
    public ItemDto addItem(ItemDto itemDto, int id) {
        Item item = itemMapper.itemDtoToItem(itemDto, id);
        items.put(item.getId(), item);
        return getItem(item.getId());
    }

    @Override
    public List<ItemDto> search(String text) {
        List<ItemDto> userItem = new ArrayList<>();
        if (text.isEmpty()) return userItem;
        for (Item i : items.values()) {
            if (i.getName().toLowerCase().contains(text.toLowerCase()) ||
                    i.getDescription().toLowerCase().contains(text.toLowerCase()) && i.isAvailable()) {
                userItem.add(itemMapper.itemToItemDto(i));
            }
        }
        return userItem;
    }

    @Override
    public ItemDto updateItem(int itemId, int id, ItemDto itemDto) {
        Item item = itemMapper.updateItem(itemDto, id, items.get(itemId));
        items.put(itemId, item);
        return itemMapper.itemToItemDto(items.get(itemId));
    }
}
