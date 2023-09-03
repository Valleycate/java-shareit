package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dao.BookingDbRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exceptions.NonexistentException;
import ru.practicum.shareit.item.dao.CommentDbRepository;
import ru.practicum.shareit.item.dao.ItemDbRepository;
import ru.practicum.shareit.item.dto.AnsItemsDto;
import ru.practicum.shareit.item.dto.AnswerCommentDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.LastBooking;
import ru.practicum.shareit.item.dto.NextBooking;
import ru.practicum.shareit.item.dto.RequestCommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserServiceImpl userService;
    private final ItemDbRepository repository;
    private final BookingDbRepository bookingRepository;
    private final CommentDbRepository commentRepository;
    private final ItemMapper mapper = Mappers.getMapper(ItemMapper.class);
    private final CommentMapper commentMapper = Mappers.getMapper(CommentMapper.class);

    public AnsItemsDto findItem(int itemId, int userId) {
        Item item = findItemById(itemId);
        if (item.getOwner().getId() == userId) {
            return setLastAndNextBookingForItem(mapper.toAnsItemsDto(findItemById(itemId), findAllCommentsByItemId(itemId)));
        }
        return mapper.toAnsItemsDto(findItemById(itemId), findAllCommentsByItemId(itemId));
    }

    public Item findItemById(int itemId) {
        Optional<Item> optionalItem = repository.findById(itemId);
        if (optionalItem.isPresent()) {
            return optionalItem.get();
        } else {
            throw new NonexistentException("такой вещи нет!");
        }
    }

    public AnswerCommentDto addComment(RequestCommentDto commentDto, int itemId, int userId) {
        User user = userService.findUserById(userId);
        Item item = findItemById(itemId);
        List<Booking> userBookings = bookingRepository.findByBooker_IdOrderByStartDesc(userId);
        List<Booking> itemBookings = bookingRepository.findByItem_IdOrderByStartDesc(itemId);
        Optional<Booking> booking = userBookings.stream()
                .filter(b -> b.getItem().equals(item) &&
                        !b.getStatus().equals(BookingStatus.REJECTED) &&
                        b.getEnd().isBefore(LocalDateTime.now()))
                .findFirst();
        if (userBookings.isEmpty() || booking.isEmpty()) {
            throw new NullPointerException("Оставить комментарий можно только для той вещи, которую вы бронировали");
        }
        if (itemBookings.isEmpty()) {
            throw new NullPointerException("Оставить комментарий можно только для той вещи, которую вы бронировали");
        }
        Comment comment = commentMapper.toComment(commentDto, LocalDateTime.now(), user, item);
        commentRepository.save(comment);
        return commentMapper.toCommentDto(comment);
    }

    public ItemDto addItem(ItemDto itemDto, int userId) {
        Item item = mapper.toItemWithCheck(itemDto, userService.findUserById(userId));
        repository.save(item);
        return mapper.toItemDto(item);
    }

    public ItemDto updateItem(ItemDto itemDto, int userId, int itemId) {
        Item item = findItemById(itemId);
        if (item.getOwner().getId() != userId) {
            throw new NonexistentException("Обновлять информацию о вещи может только её владелец");
        }
        Item itemUpdate = mapper.toItemWithoutCheck(itemDto, userService.findUserById(userId));
        if (itemUpdate.getAvailable() != null) {
            item.setAvailable(itemUpdate.getAvailable());
        }
        if (itemUpdate.getDescription() != null) {
            item.setDescription(itemUpdate.getDescription());
        }
        if (itemUpdate.getName() != null) {
            item.setName(itemUpdate.getName());
        }
        repository.save(item);
        return mapper.toItemDto(item);
    }

    public List<AnsItemsDto> findAllItemsByUser(int userId) {
        userService.findUserById(userId);
        return setLastAndNextBookingForAllItems((repository.findAllByOwnerId(userId).stream()
                .map(item -> mapper.toAnsItemsDto(item, findAllCommentsByItemId(item.getId())))
                .collect(Collectors.toList())));
    }

    public List<ItemDto> findAllItemsByUserForBooking(int userId) {
        userService.findUserById(userId);
        return repository.findAllByOwnerId(userId).stream()
                .map(mapper::toItemDto)
                .collect(Collectors.toList());
    }

    public List<ItemDto> searchItem(String text) {
        if (text.isBlank() || text.isEmpty()) {
            return new ArrayList<>();
        }
        return repository.search(text).stream()
                .filter(Item::getAvailable)
                .map(mapper::toItemDto)
                .collect(Collectors.toList());
    }

    private List<AnsItemsDto> setLastAndNextBookingForAllItems(List<AnsItemsDto> items) {
        items.forEach(this::setLastAndNextBookingForItem);
        return items;
    }

    private AnsItemsDto setLastAndNextBookingForItem(AnsItemsDto item) {
        List<Booking> bookings = new ArrayList<>(bookingRepository.findByItem_IdOrderByStartDesc(item.getId()));
        bookings.forEach(booking -> {
            if (item.getNextBooking() != null && booking.getStart().isAfter(LocalDateTime.now())
                    && !booking.getStatus().equals(BookingStatus.REJECTED)) {
                if (item.getNextBooking().getStart().isAfter(booking.getStart())) {
                    item.getNextBooking().setId(booking.getId());
                    item.getNextBooking().setBookerId(booking.getBooker().getId());
                    item.getNextBooking().setStart(booking.getStart());
                }
            } else if (item.getNextBooking() == null && booking.getStart().isAfter(LocalDateTime.now())
                    && !booking.getStatus().equals(BookingStatus.REJECTED)) {
                item.setNextBooking(new NextBooking());
                item.getNextBooking().setId(booking.getId());
                item.getNextBooking().setBookerId(booking.getBooker().getId());
                item.getNextBooking().setStart(booking.getStart());
            }
            if (item.getLastBooking() != null && booking.getStart().isBefore(LocalDateTime.now())
                    && !booking.getStatus().equals(BookingStatus.REJECTED)) {
                if (item.getLastBooking().getEnd().isBefore(booking.getEnd())) {
                    item.getLastBooking().setId(booking.getId());
                    item.getLastBooking().setBookerId(booking.getBooker().getId());
                    item.getLastBooking().setEnd(booking.getEnd());
                }
            } else if (item.getLastBooking() == null && booking.getStart().isBefore(LocalDateTime.now())
                    && !booking.getStatus().equals(BookingStatus.REJECTED)) {
                item.setLastBooking(new LastBooking());
                item.getLastBooking().setId(booking.getId());
                item.getLastBooking().setBookerId(booking.getBooker().getId());
                item.getLastBooking().setEnd(booking.getEnd());
            }
        });
        return item;
    }

    private List<AnswerCommentDto> findAllCommentsByItemId(int itemId) {
        return commentRepository.findByItem_Id(itemId).stream()
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toList());
    }
}
