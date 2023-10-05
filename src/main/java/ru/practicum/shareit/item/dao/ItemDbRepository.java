package ru.practicum.shareit.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemDbRepository extends JpaRepository<Item, Integer> {
    List<Item> findAllByOwnerId(Integer ownerId);

    List<Item> findAllByRequestId(Integer requestId);

    @Query(value = "select * from items as i " +
            "where (upper(i.name) like upper(concat('%', ?1, '%')) " +
            " or upper(i.description) like upper(concat('%', ?1, '%')))", nativeQuery = true)
    List<Item> search(String text);
}
