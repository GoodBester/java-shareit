package ru.practicum.shareit.item.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingReturnDtoForItem;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.DAO.ItemRepository;
import ru.practicum.shareit.item.DAO.CommentRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemReturnDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.DAO.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final ModelMapper mapper;


    @Override
    @Transactional
    public List<ItemReturnDto> getUserItem(long id, Pageable page) {
        User user = userService.getUserEntity(id);
        return itemRepository.findAllByOwner_Id(id, page).stream().map(i -> mapToItemReturnDto(i, true)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ItemReturnDto getItem(long itemId, long userId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "Item not found."));
        User user = userService.getUserEntity(userId);
        return mapToItemReturnDto(item, user.getId() == item.getOwner().getId());
    }

    @Override
    public ItemDto addItem(ItemDto itemDto, long id) {
        userService.getUser(id);
        return mapToDto(itemRepository.save(matToItem(itemDto, id)));
    }

    @Override
    @Transactional
    public CommentDto addComment(CommentDto commentDto, long userId, long itemId) {
        if (bookingRepository.findFirstByBooker_IdAndItem_IdAndStatusAndEndBefore(userId, itemId, Status.APPROVED, LocalDateTime.now()) == null) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "User did not book this item");
        }
        User user = userService.getUserEntity(userId);
        Item item = getItemEntity(itemId);
        commentDto.setAuthorName(user.getName());
        Comment com = mapper.map(commentDto, Comment.class);
        com.setCommentator(user);
        com.setItem(item);
        com.setCreated(LocalDateTime.now());
        return mapToCommentDto(commentRepository.save(com));
    }

    @Override
    public List<ItemDto> search(String text, Pageable page) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        List<Item> itemList = itemRepository.findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailable(
                text, text, true, page);
        return itemList.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public ItemDto updateItem(long itemId, long id, ItemDto itemDto) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "Item not found."));
        if (item.getOwner().getId() != id)
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Item does not own to this user");
        if (itemDto.getName() != null) item.setName(itemDto.getName());
        if (itemDto.getDescription() != null) item.setDescription(itemDto.getDescription());
        if (itemDto.getAvailable() != null) item.setAvailable(itemDto.getAvailable());
        return mapToDto(itemRepository.save(item));
    }

    private ItemDto mapToDto(Item item) {
        ItemDto dto = mapper.map(item, ItemDto.class);
        if (item.getRequest() != null) {
            dto.setRequestId(item.getRequest().getId());
        }
        return dto;
    }

    private Item matToItem(ItemDto itemDto, long id) {
        itemDto.setOwner(userService.getUserEntity(id));
        itemDto.setId(0);
        Item item = mapper.map(itemDto, Item.class);
        if (itemDto.getRequestId() != 0) {
            item.setRequest(itemRequestRepository.findById(itemDto.getRequestId()).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "request not found.")));
        }
        return item;
    }

    private CommentDto mapToCommentDto(Comment comment) {
        CommentDto commentDto = mapper.map(comment, CommentDto.class);
        commentDto.setAuthorName(comment.getCommentator().getName());
        commentDto.setCreated(comment.getCreated());
        return commentDto;
    }

    @Override
    public Item getItemEntity(long id) {
        return itemRepository.findById(id).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "Item not found."));
    }

    @Override
    public List<Item> findAllByRequest_Id(Long requestId) {
        return itemRepository.findAllByRequest_Id(requestId);
    }

    private BookingReturnDtoForItem convertToBookingDtoForItem(Booking opt, boolean isOwner) {
        if (opt != null && isOwner) {
            BookingReturnDtoForItem booking = mapper.map(opt, BookingReturnDtoForItem.class);
            booking.setBookerId(opt.getBooker().getId());
            return booking;
        } else {
            return null;
        }
    }

    private ItemReturnDto mapToItemReturnDto(Item item, boolean isOwner) {
        ItemReturnDto iDto = mapper.map(item, ItemReturnDto.class);
        Booking lastBooking = bookingRepository.findFirstByItem_IdAndStatusAndStartBeforeOrderByEndDesc(item.getId(), Status.APPROVED, LocalDateTime.now());
        Booking nextBooking = bookingRepository.findFirstByItem_IdAndStatusAndStartAfterOrderByStartAsc(item.getId(), Status.APPROVED, LocalDateTime.now());
        iDto.setComments(commentRepository.findAllByItem_Id(item.getId()).stream().map(this::mapToCommentDto).collect(Collectors.toList()));
        iDto.setLastBooking(convertToBookingDtoForItem(lastBooking, isOwner));
        iDto.setNextBooking(convertToBookingDtoForItem(nextBooking, isOwner));
        return iDto;
    }


}
