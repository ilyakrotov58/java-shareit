package ru.practicum.shareit.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface IItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    @Query("SELECT itemRequest FROM ItemRequest itemRequest " +
            "WHERE itemRequest.userId = ?1 " +
            "ORDER BY itemRequest.created DESC")
    List<ItemRequest> getRequests(long userId);

    @Query("SELECT itemRequest FROM ItemRequest itemRequest " +
            "WHERE itemRequest.userId <> ?1 " +
            "ORDER BY itemRequest.created DESC")
    List<ItemRequest> getItemRequestsOtherUsers(long userId);
}
