package ru.practicum.shareit.request.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.IItemRequestService;

import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@Tag(name = "Requests")
public class ItemRequestController {

    @Autowired
    private IItemRequestService itemRequestService;

    @PostMapping
    @Operation(summary = "Create request")
    public ItemRequestDto createRequest(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestService.add(userId, itemRequestDto);
    }

    @GetMapping()
    @Operation(summary = "Get requests")
    public List<ItemRequestDto> getRequest(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemRequestService.get(userId);
    }

    @GetMapping("/all")
    @Operation(summary = "Get other users requests")
    public List<ItemRequestDto> getUsersRequests(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(defaultValue = "0") @PositiveOrZero long from,
            @RequestParam(defaultValue = "10") long size) {
        return itemRequestService.getUsersRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    @Operation(summary = "Get request by id")
    public ItemRequestDto getItemRequestById(
            @PathVariable long requestId,
            @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemRequestService.getById(requestId, userId);
    }
}
