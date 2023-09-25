package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoAns;

import java.util.List;

import static ru.practicum.shareit.item.controller.XHeaderUserId.X_SHARER_USER_ID;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService service;

    @PostMapping()
    public ItemRequestDtoAns addRequest(@RequestBody ItemRequestDto description, @RequestHeader(X_SHARER_USER_ID) Integer userId) {
        return service.addRequest(description, userId);
    }

    @GetMapping()
    public List<ItemRequestDtoAns> findRequestsByUserId(@RequestHeader(X_SHARER_USER_ID) Integer userId) {
        return service.findRequestsByUserId(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDtoAns> findAllRequests(@RequestParam(required = false, defaultValue = "0") int from,
                                                   @RequestParam(required = false, defaultValue = "10000") int size,
                                                   @RequestHeader(X_SHARER_USER_ID) Integer userId) {
        return service.findAllRequests(from, size, userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDtoAns findRequest(@PathVariable int requestId, @RequestHeader(X_SHARER_USER_ID) Integer userId) {
        return service.findRequestById(requestId, userId);
    }
}
