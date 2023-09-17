package ru.practicum.shareit.request;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface RequestDbRepository extends JpaRepository<ItemRequest, Integer> {
    List<ItemRequest> findAllByRequesterId(Integer requesterId);
    List<ItemRequest> findAllByOrderByCreatedDesc(Pageable pageable);
}
