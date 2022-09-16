package ru.practicum.shareit.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.IItemService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private IItemService service;

    @PostMapping
    public ItemDto add(@Valid @RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") long userId) {
        itemDto.setUserId(userId);
        return service.add(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto edit(
            @RequestBody ItemDto item,
            @PathVariable long itemId,
            @RequestHeader("X-Sharer-User-Id") long userId) {
        return service.edit(item, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getById(@PathVariable long itemId) {
        return service.getById(itemId);
    }

    @DeleteMapping("/{itemId}")
    public void deleteById(@PathVariable long itemId,
                           @RequestHeader("X-Sharer-User-Id") long userId) {
        service.deleteById(itemId, userId);
    }

    @GetMapping("/search")
    public List<ItemDto> getAllByText(@RequestParam String text) {
        return service.getAllByText(text);
    }

    @GetMapping
    public List<ItemDto> getAll(@RequestHeader("X-Sharer-User-Id") long userId) {
        return service.getAll(userId);
    }
}
