package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NonexistentException;
import ru.practicum.shareit.item.dao.ItemDbRepository;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.request.dao.RequestDbRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoAns;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemMapper itemMapper = Mappers.getMapper(ItemMapper.class);
    private final RequestDbRepository repository;
    private final UserService userService;
    private final ItemDbRepository itemRepository;
    private final ItemRequestMapper mapper = Mappers.getMapper(ItemRequestMapper.class);

    public ItemRequestDtoAns addRequest(ItemRequestDto description, int userId) {
        if (description.getDescription() == null || description.getDescription().isBlank()) {
            throw new NullPointerException("описание не может быть пустым");
        }
        ItemRequest request = new ItemRequest();
        request.setCreated(LocalDateTime.now());
        request.setRequester(userService.findUserById(userId));
        request.setDescription(description.getDescription());
        repository.save(request);
        return mapper.toDto(request);
    }

    public List<ItemRequestDtoAns> findRequestsByUserId(int userId) {
        userService.findUserById(userId);
        List<ItemRequestDtoAns> requests = repository.findAllByRequesterId(userId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return setItemsForRequest(requests);
    }

    public List<ItemRequestDtoAns> findAllRequests(int from, int size, int userId) {
        int page;
        if (from < 0) {
            page = from;
        } else {
            page = from / size;
        }
        userService.findUserById(userId);
        List<ItemRequest> requests = repository.findAllByOrderByCreatedDesc(PageRequest.of(page, size));
        return setItemsForRequest(requests.stream()
                .filter(itemRequest -> itemRequest.getRequester().getId() != userId)
                .map(mapper::toDto)
                .collect(Collectors.toList()));

    }

    public ItemRequestDtoAns findRequestById(int id, int userId) {
        userService.findUserById(userId);
        Optional<ItemRequest> optionalRequest = repository.findById(id);
        if (optionalRequest.isEmpty()) {
            throw new NonexistentException("запроса по такому id нет!");
        }
        return setItemsForRequest(List.of(mapper.toDto(optionalRequest.get()))).get(0);
    }

    private List<ItemRequestDtoAns> setItemsForRequest(List<ItemRequestDtoAns> requests) {
        requests.forEach(request -> request.setItems(itemRepository.findAllByRequestId(request.getId()).stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList())));
        return requests;
    }
}
