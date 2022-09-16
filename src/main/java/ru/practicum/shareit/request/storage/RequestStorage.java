package ru.practicum.shareit.request.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.HashMap;
import java.util.Map;

@Component
public class RequestStorage implements IItemRequestStorage {

    private final Map<Long, ItemRequest> inMemoryItemRequest = new HashMap<>();

    private final long id = 0;
}
