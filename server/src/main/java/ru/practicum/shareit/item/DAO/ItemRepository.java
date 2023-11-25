package ru.practicum.shareit.item.DAO;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;


public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailable(
            String name, String description, Boolean available, Pageable page);

    List<Item> findAllByOwner_IdOrderById(Long id, Pageable page);

    List<Item> findAllByRequest_Id(Long requestId);
}


