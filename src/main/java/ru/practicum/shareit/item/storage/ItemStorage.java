package ru.practicum.shareit.item.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.AvailableStatusIsFalseException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.UserIsNotOwnerException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.storage.IUserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ItemStorage implements IItemStorage {

    @Autowired
    private IUserStorage userStorage;

    private final Map<Long, Item> inMemoryItemStorage = new HashMap<>();
    private long id = 0;

    @Override
    public ItemDto add(ItemDto itemDto, long userId) {
        validateUserId(userId);
        if (!itemDto.getAvailable()) {
            throw new AvailableStatusIsFalseException("Can't create item with not available status");
        }

        itemDto.setUserId(userId);

        var item = ItemDtoMapper.fromDto(itemDto);
        item.setId(++id);

        inMemoryItemStorage.put(item.getId(), item);

        return ItemDtoMapper.toDto(item);
    }

    @Override
    public ItemDto edit(ItemDto itemDto, long itemId, long userId) {

        validateItemInDb(itemId);
        validateUserId(userId);
        validateOwner(itemId, userId);

        var item = inMemoryItemStorage.get(itemId);

        itemDto.setUserId(userId);
        if (itemDto.getName() != null) item.setName(itemDto.getName());
        if (itemDto.getDescription() != null) item.setDescription(itemDto.getDescription());
        if (itemDto.getAvailable() != null) item.setAvailable(itemDto.getAvailable());

        inMemoryItemStorage.replace(itemId, item);

        return ItemDtoMapper.toDto(item);
    }

    @Override
    public List<ItemDto> getAllByText(String text) {
        var textLower = text.toLowerCase();

        List<ItemDto> result = new ArrayList<>();

        if(!text.isEmpty()){
            result = inMemoryItemStorage.entrySet().stream()
                    .filter(item -> (
                            item.getValue().getDescription().toLowerCase().contains(textLower)
                            || item.getValue().getName().toLowerCase().contains(textLower))
                            && item.getValue().getAvailable())
                    .map(Map.Entry::getValue)
                    .map(ItemDtoMapper::toDto)
                    .collect(Collectors.toList());
        }

        return result;
    }

    @Override
    public List<ItemDto> getAll(long userId) {
        validateUserId(userId);

        return inMemoryItemStorage.entrySet().stream()
                .filter(i -> i.getValue().getUserId() == userId)
                .map(Map.Entry::getValue)
                .map(ItemDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getById(long itemId) {
        validateItemInDb(itemId);

        var item = inMemoryItemStorage.get(itemId);
        return ItemDtoMapper.toDto(item);
    }

    @Override
    public void deleteById(long itemId, long userId) {
        validateUserId(userId);
        validateItemInDb(itemId);
        validateOwner(itemId, userId);

        inMemoryItemStorage.remove(itemId);
    }

    private void validateUserId(long userId) {
        if (!userStorage.getAllUserIds().contains(userId)) {
            throw new NotFoundException(String.format("User with id  =%s is not exists", userId));
        }
    }

    private void validateOwner(long itemId, long ownerId) {
        if (inMemoryItemStorage.get(itemId).getUserId() != ownerId) {
            throw new UserIsNotOwnerException("You can't change item, because you are not owner");
        }
    }

    private void validateItemInDb(long itemId) {
        if (inMemoryItemStorage.get(itemId) == null) {
            throw new NotFoundException(String.format("Item with id = %s can't be found", itemId));
        }
    }
}
