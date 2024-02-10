package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.DAO.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemDtoForRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestReturnDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserService userService;
    private final ModelMapper mapper;
    private final ItemService itemService;


    @Override
    public ItemRequestDto addRequest(long userId, ItemRequestDto dto) {
        User requester = userService.getUserEntity(userId);
        dto.setCreated(LocalDateTime.now());
        ItemRequest ir = mapToEntity(dto);
        ir.setRequester(requester);
        return mapToDto(itemRequestRepository.save(ir));
    }

    @Override
    public List<ItemRequestReturnDto> getRequests(long userId) {
        userService.getUserEntity(userId);
        List<ItemRequestReturnDto> list = itemRequestRepository.findAllByRequester_IdOrderByCreatedDesc(userId).stream()
                        .map(this::mapToReturnDto).collect(Collectors.toList());
        return list;
    }

    @Override
    public List<ItemRequestReturnDto> getAllRequests(long userId, Pageable page) {
        List<ItemRequestReturnDto> list = itemRequestRepository.findAllByRequester_IdIsNot(userId, page).stream()
                .map(this::mapToReturnDto).collect(Collectors.toList());
        return list;
    }

    @Override
    public ItemRequest getEntityByUId(long id) {
        return itemRequestRepository.findById(id).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "Request not found."));
    }

    @Override
    public ItemRequestReturnDto getRequest(long userId, long requestId) {
        userService.getUserEntity(userId);
        ItemRequestReturnDto req = mapToReturnDto(getEntityByUId(requestId));
        return req;
    }

    private ItemRequest mapToEntity(ItemRequestDto dto) {
        return mapper.map(dto, ItemRequest.class);
    }

    private ItemRequestDto mapToDto(ItemRequest entity) {
        return mapper.map(entity, ItemRequestDto.class);
    }

    private ItemDtoForRequest mapToItemReturnDto(Item item) {
        ItemDtoForRequest dto = mapper.map(item, ItemDtoForRequest.class);
        dto.setRequestId(item.getRequest().getId());
        return dto;
    }

    private ItemRequestReturnDto mapToReturnDto(ItemRequest ir) {
        ItemRequestReturnDto dto = mapper.map(ir, ItemRequestReturnDto.class);
        dto.setRequesterId(ir.getRequester().getId());
        dto.setItems(setItems(ir.getId()));
        return dto;
    }

    private List<ItemDtoForRequest> setItems(long reqId) {
        return new ArrayList<>(itemService.findAllByRequest_Id(reqId).stream().map(this::mapToItemReturnDto).collect(Collectors.toList()));
    }
}
