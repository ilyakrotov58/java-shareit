package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface IItemStorage {

    ItemDto add(ItemDto item, long userId);

    ItemDto edit(ItemDto item, long itemId, long userId);

    List<ItemDto> getAllByText(String text);

    List<ItemDto> getAll(long userId);

    ItemDto getById(long itemId);

    void deleteById(long itemId, long userId);
}
