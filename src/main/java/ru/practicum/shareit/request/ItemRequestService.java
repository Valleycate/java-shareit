package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoAns;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDtoAns addRequest(ItemRequestDto description, int userId);

    List<ItemRequestDtoAns> findRequestsByUserId(int userId);

    List<ItemRequestDtoAns> findAllRequests(int from, int size, int userId);

    ItemRequestDtoAns findRequestById(int id, int userId);
}
