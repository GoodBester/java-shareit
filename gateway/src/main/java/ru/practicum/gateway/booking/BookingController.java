package ru.practicum.gateway.booking;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.gateway.booking.dto.BookingDto;
import ru.practicum.gateway.exception.IncorrectStateException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;


@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
@Validated
public class BookingController {
    private final ru.practicum.gateway.booking.BookingClient bookingClient;
    public static final String header = "X-Sharer-User-Id";

    @GetMapping
    public ResponseEntity<Object> getAllBookings(@RequestHeader(header) long userId,
                                                 @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
                                                 @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                 @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        validState(stateParam);
        log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.getBookings(userId, stateParam, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(header) long userId,
                                         @RequestBody @Valid BookingDto requestDto) {
        log.info("Creating booking {}, userId={}", requestDto, userId);
        return bookingClient.bookItem(userId, requestDto);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader(header) long userId,
                                             @PathVariable Long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }


    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> setApproved(@RequestHeader(header) long userId,
                                              @PathVariable("bookingId") long bookingId,
                                              @RequestParam("approved") Boolean approved) {
        log.info("A request to the endpoint was received /bookings updateStatus  headers {},  bookingId {}, status {}",
                userId, bookingId, approved);
        return bookingClient.setApproved(userId, bookingId, approved);
    }


    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingsForOwner(@RequestHeader(header) long userId,
                                                      @RequestParam(value = "state", defaultValue = "ALL") String stateParam,
                                                      @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                      @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        validState(stateParam);
        log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.getBookingsForOwner(userId, stateParam, from, size);
    }

    private void validState(String state) {
        if (state.equals("ALL") || state.equals("FUTURE") || state.equals("PAST") ||
                state.equals("REJECTED") || state.equals("CURRENT") || state.equals("WAITING")) {
            return;
        }
        throw new IncorrectStateException();
    }
}