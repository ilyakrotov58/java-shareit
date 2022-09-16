package ru.practicum.shareit.item.service;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import java.util.List;

public interface IItemService {

    ItemDto add(@Valid
                @RequestBody ItemDto item,
                @RequestHeader("X-Sharer-User-Id") long userId);

    ItemDto edit(@Valid
                 @RequestBody ItemDto itemDto,
                 @PathVariable long itemId,
                 @RequestHeader("X-Sharer-User-Id") long userId);

    ItemDto getById(@Valid @PathVariable long itemId);

    void deleteById(@PathVariable long itemId,
                    @RequestHeader("X-Sharer-User-Id") long userId);

    List<ItemDto> getAllByText(@RequestParam(required = false) String text);

    List<ItemDto> getAll(@RequestHeader("X-Sharer-User-Id") long userId);
}
