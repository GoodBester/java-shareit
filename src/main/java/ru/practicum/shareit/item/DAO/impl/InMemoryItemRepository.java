package ru.practicum.shareit.item.DAO.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.DAO.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class InMemoryItemRepository implements ItemRepository {

    private final ItemMapper itemMapper;

    private final Map<Integer, Item> items = new HashMap<>();


    @Override
    public List<ItemDto> getUserItem(int id) {
        return items.values().stream().filter(item -> item.getOwner() == id).map(itemMapper::itemToItemDto).collect(Collectors.toList());
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
        return items.values().stream().filter(item -> (item.getName().toLowerCase().contains(text.toLowerCase()) ||
                        item.getDescription().toLowerCase().contains(text.toLowerCase()) && item.isAvailable()) && !text.isEmpty())
                .map(itemMapper::itemToItemDto).collect(Collectors.toList());
    }

    @Override
    public ItemDto updateItem(int itemId, int id, ItemDto itemDto) {
        Item item = itemMapper.updateItem(itemDto, id, items.get(itemId));
        items.put(itemId, item);
        return itemMapper.itemToItemDto(items.get(itemId));
    }
}
