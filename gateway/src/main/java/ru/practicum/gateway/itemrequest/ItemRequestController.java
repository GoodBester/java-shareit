package ru.practicum.gateway.itemrequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.gateway.itemrequest.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {
    private final ru.practicum.gateway.itemrequest.ItemRequestClient itemRequestClient;
    public static final String header = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> addRequest(@RequestHeader(header) long userId,
                                             @RequestBody @Valid ItemRequestDto itemRequestDto) {
        log.info("Creating itemRequest {}, userId={}", itemRequestDto, userId);
        return itemRequestClient.addRequest(userId, itemRequestDto);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(@RequestHeader(header) long userId,
                                                 @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                 @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get all requests with  userId={}, from={}, size={}", userId, from, size);
        return itemRequestClient.getAllRequests(userId, from, size);
    }

    @GetMapping
    public ResponseEntity<Object> getRequests(@RequestHeader(header) int userId) {
        log.info("get itemRequest, userId={}", userId);
        return itemRequestClient.getRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequest(@RequestHeader(header) int userId,
                                             @PathVariable("requestId") int requestId) {
        log.info("get itemRequest {}, userId={}", requestId, userId);
        return itemRequestClient.getRequest(userId, requestId);
    }

}
