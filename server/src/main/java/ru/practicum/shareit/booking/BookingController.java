package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingReturnDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

import static ru.practicum.shareit.ShareItApp.getPage;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {

    private BookingService bookingService;
    private final String header = "X-Sharer-User-Id";

    @PostMapping
    public BookingReturnDto addBooking(@RequestHeader(name = header) Long userId,
                                       @Valid @RequestBody BookingDto dto) {
        return bookingService.create(dto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingReturnDto setApproved(@RequestHeader(name = header) Long userId,
                                        @PathVariable("bookingId") Long bookingId,
                                        @RequestParam("approved") Boolean approved) {
        return bookingService.setApproved(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingReturnDto getBooking(@RequestHeader(name = header) Long userId,
                                       @PathVariable("bookingId") Long bookingId) {
        return bookingService.getBooking(userId, bookingId);
    }

    @GetMapping
    public List<BookingReturnDto> getAllBookingFromUser(@RequestHeader(name = header) Long userId,
                                                        @RequestParam(value = "state", defaultValue = "ALL") String state,
                                                        @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                        @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Pageable page = getPage(from, size);
        return bookingService.getAllBookingFromUser(userId, state, "user", page);
    }

    @GetMapping("/owner")
    public List<BookingReturnDto> getAllBookingFromOwner(@RequestHeader(name = header) Long userId,
                                                         @RequestParam(value = "state", defaultValue = "ALL") String state,
                                                         @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                         @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Pageable page = getPage(from, size);
        return bookingService.getAllBookingFromUser(userId, state, "owner", page);
    }
}
