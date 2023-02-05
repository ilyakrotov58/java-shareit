package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface IItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT item FROM Item item " +
            "WHERE item.userId = ?1 " +
            "ORDER BY item.id")
    List<Item> getAll(long userId);

    @Query("SELECT item FROM Item item " +
            "WHERE item.requestId = ?1 ")
    List<Item> getItemsByRequestId(long requestId);
}
