package shareit.item.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import shareit.item.dto.CommentDto;
import shareit.item.dto.ItemDto;
import shareit.item.service.ItemService;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping("/items")
@Tag(name = "Items")
public class ItemController {

    @Autowired
    private ItemService service;

    @GetMapping("/{itemId}")
    @Operation(summary = "Get Item by id")
    public ResponseEntity<Object> getById(@PathVariable long itemId, @RequestHeader("X-Sharer-User-Id") long userId) {
        return service.getById(itemId, userId);
    }

    @PostMapping
    @Operation(summary = "Add Item")
    public ResponseEntity<Object> add(@Valid @RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") long userId) {
        itemDto.setUserId(userId);
        return service.add(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    @Operation(summary = "Edit Item")
    public ResponseEntity<Object> edit(
            @RequestBody ItemDto item,
            @PathVariable long itemId,
            @RequestHeader("X-Sharer-User-Id") long userId) {
        return service.edit(item, itemId, userId);
    }

    @DeleteMapping("/{itemId}")
    @Operation(summary = "Delete item")
    public void deleteById(@PathVariable long itemId,
                           @RequestHeader("X-Sharer-User-Id") long userId) {
        service.deleteById(itemId, userId);
    }

    @GetMapping("/search")
    @Operation(summary = "Search items by text in name or description")
    public ResponseEntity<Object> getAllByText(@RequestParam(name = "text") String text,
                                               @RequestParam(defaultValue = "0") long index,
                                               @RequestParam(defaultValue = "10") long size) {
        return service.getAllByText(text, index, size);
    }

    @GetMapping
    @Operation(summary = "Search all items for certain user")
    public ResponseEntity<Object> getAll(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @RequestParam(defaultValue = "0") long index,
                                         @RequestParam(defaultValue = "10") long size) {
        return service.getAll(userId, index, size);
    }

    @PostMapping("/{itemId}/comment")
    @Operation(summary = "Add comment")
    public ResponseEntity<Object> addComment(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestBody CommentDto comment,
            @PathVariable long itemId) {
        return service.addComment(userId, itemId, comment);
    }
}
