package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {


    List<Booking> findAllByBooker_IdOrderByStartDesc(Long userId);
    List<Booking> findAllBookingByItem_Owner_IdOrderByStartDesc(Long userId);
    List<Booking> findAllBookingByItem_Owner_IdAndStartAfterOrderByStartDesc(Long userId, LocalDateTime now);
    List<Booking> findAllBookingByBookerIdAndStartAfterOrderByStartDesc(Long userId, LocalDateTime now);
    List<Booking> findAllBookingByItem_Owner_IdAndEndBeforeOrderByStartDesc(Long userId, LocalDateTime now);
    List<Booking> findAllBookingByBookerIdAndEndBeforeOrderByStartDesc(Long userId, LocalDateTime now);
    List<Booking> findAllBookingByItem_Owner_IdAndStartBeforeAndEndAfterOrderByStartDesc(Long userId, LocalDateTime now, LocalDateTime now2);
    List<Booking> findAllBookingByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long userId, LocalDateTime now, LocalDateTime now2);
    List<Booking> findAllBookingByItem_Owner_IdAndStatusOrderByStartDesc(Long userId, Status state);
    List<Booking> findAllBookingByBookerIdAndStatusOrderByStartDesc(Long userId, Status state);
    Booking findFirstByBooker_IdAndItem_IdAndStatusAndEndBefore(Long userId, Long itemId, Status status, LocalDateTime now);
    Booking findFirstByItem_IdAndStatusAndStartBeforeOrderByEndDesc(Long itemId, Status status, LocalDateTime now2);
    Booking findFirstByItem_IdAndStatusAndStartAfterOrderByStartAsc(Long itemId, Status status, LocalDateTime now);
}
