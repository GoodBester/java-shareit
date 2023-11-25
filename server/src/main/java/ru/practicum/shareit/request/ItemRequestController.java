package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestReturnDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

import static ru.practicum.shareit.ShareItApp.getPage;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto addRequest(@RequestHeader("X-Sharer-User-Id") int userId,
                                     @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestService.addRequest(userId, itemRequestDto);
    }

    @GetMapping
    public List<ItemRequestReturnDto> getRequests(@RequestHeader("X-Sharer-User-Id") int userId) {
        return itemRequestService.getRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestReturnDto> getAllRequests(@RequestHeader("X-Sharer-User-Id") int userId,
                                                     @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                     @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Pageable page = getPage(from, size);
        return itemRequestService.getAllRequests(userId, page);
    }

    @GetMapping("/{requestId}")
    public ItemRequestReturnDto getRequest(@RequestHeader("X-Sharer-User-Id") int userId,
                                           @PathVariable("requestId") int requestId) {

        return itemRequestService.getRequest(userId, requestId);
    }

}
