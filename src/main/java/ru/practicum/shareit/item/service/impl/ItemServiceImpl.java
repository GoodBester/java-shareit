package ru.practicum.shareit.item.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.DAO.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }


    @Override
    public List<ItemDto> getUserItem(int id) {
        return itemRepository.getUserItem(id);
    }

    @Override
    public ItemDto getItem(int id) {
        return itemRepository.getItem(id);
    }

    @Override
    public ItemDto addItem(ItemDto itemDto, int id) {
        return itemRepository.addItem(itemDto, id);
    }

    @Override
    public List<ItemDto> search(String text) {
        return itemRepository.search(text);
    }

    @Override
    public ItemDto updateItem(int itemId, int id, ItemDto itemDto) {
        return itemRepository.updateItem(itemId, id, itemDto);
    }


}
