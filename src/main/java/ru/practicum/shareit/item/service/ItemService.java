package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.storage.IItemStorage;

import java.util.List;

@Service
@Slf4j
public class ItemService implements IItemService {

    @Autowired
    private IItemStorage iItemStorage;

    @Override
    public ItemDto add(ItemDto item, long userId) {
        var addedItem = iItemStorage.add(item, userId);
        log.info(String.format("Item with id =%s was added", addedItem.getId()));

        return addedItem;
    }

    @Override
    public ItemDto edit(ItemDto itemDto, long itemId, long userId) {
        var editedItem = iItemStorage.edit(itemDto, itemId, userId);
        log.info(String.format("Item with id =%s was edited", editedItem.getId()));

        return editedItem;
    }

    @Override
    public ItemDto getById(long itemId) {
        return iItemStorage.getById(itemId);
    }

    @Override
    public void deleteById(long itemId, long userId) {
        iItemStorage.deleteById(itemId, userId);
        log.info(String.format("Item with id =%s was deleted", itemId));
    }

    @Override
    public List<ItemDto> getAllByText(String text) {
        return iItemStorage.getAllByText(text);
    }

    @Override
    public List<ItemDto> getAll(long userId) {
        return iItemStorage.getAll(userId);
    }
}
