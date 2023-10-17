package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerOrderByStartDesc(User user);

    List<Booking> findAllBookingByItem_OwnerOrderByStartDesc(User user);

    List<Booking> findAllBookingByItem_OwnerAndStartAfterOrderByStartDesc(User user, LocalDateTime now);

    List<Booking> findAllBookingByBookerAndStartAfterOrderByStartDesc(User user, LocalDateTime now);

    List<Booking> findAllBookingByItem_OwnerAndEndBeforeOrderByStartDesc(User user, LocalDateTime now);

    List<Booking> findAllBookingByBookerAndEndBeforeOrderByStartDesc(User user, LocalDateTime now);

    List<Booking> findAllBookingByItem_OwnerAndStartBeforeAndEndAfterOrderByStartDesc(User user, LocalDateTime now, LocalDateTime now2);

    List<Booking> findAllBookingByBookerAndStartBeforeAndEndAfterOrderByStartDesc(User user, LocalDateTime now, LocalDateTime now2);

    List<Booking> findAllBookingByItem_OwnerAndStatusOrderByStartDesc(User user, Status state);

    List<Booking> findAllBookingByBookerAndStatusOrderByStartDesc(User user, Status state);

    Booking findFirstByBooker_IdAndItem_IdAndStatusAndEndBefore(Long userId, Long itemId, Status status, LocalDateTime now);

    Booking findFirstByItemAndStatusAndStartBeforeOrderByEndDesc(Item item, Status status, LocalDateTime now2);

    Booking findFirstByItemAndStatusAndStartAfterOrderByStartAsc(Item item, Status status, LocalDateTime now);


}
