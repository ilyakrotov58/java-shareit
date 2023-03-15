package shareit.request.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import shareit.request.dto.ItemRequestDto;
import shareit.request.service.ItemRequestService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping(path = "/requests")
@Tag(name = "Requests")
public class ItemRequestController {

    @Autowired
    private ItemRequestService itemRequestService;

    @PostMapping
    @Operation(summary = "Create request")
    public ResponseEntity<Object> createRequest(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestService.add(userId, itemRequestDto);
    }

    @GetMapping()
    @Operation(summary = "Get requests")
    public ResponseEntity<Object> getRequest(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemRequestService.get(userId);
    }

    @GetMapping("/all")
    @Operation(summary = "Get other users requests")
    public ResponseEntity<Object> getUsersRequests(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(defaultValue = "0") @PositiveOrZero long from,
            @RequestParam(defaultValue = "10")
            @Positive long size) {
        return itemRequestService.getUsersRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    @Operation(summary = "Get request by id")
    public ResponseEntity<Object> getItemRequestById(
            @PathVariable long requestId,
            @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemRequestService.getById(requestId, userId);
    }
}
