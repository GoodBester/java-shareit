package ru.practicum.shareit.request.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestReturnDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto addRequest(long userId, ItemRequestDto dto);

    List<ItemRequestReturnDto> getRequests(long userId);

    List<ItemRequestReturnDto> getAllRequests(long userId, Pageable page);

    ItemRequest getEntityByUId(long id);

    ItemRequestReturnDto getRequest(long userId, long requestId);
}
