package ru.practicum.shareit.request.DAO;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findAllByRequester_IdOrderByCreatedDesc(Long userId);


    List<ItemRequest> findAllByRequester_IdIsNot(Long userId, Pageable page);

}
