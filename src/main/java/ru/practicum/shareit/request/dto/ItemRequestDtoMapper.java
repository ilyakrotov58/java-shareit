package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.request.model.ItemRequest;

public class ItemRequestDtoMapper {

    public static ItemRequestDto toDto(ItemRequest itemRequest) {
        return new ItemRequestDto(
                itemRequest.getId(),
                itemRequest.getUserId(),
                itemRequest.getItemName(),
                itemRequest.getItemDescription()
        );
    }

    public static ItemRequest fromDto(ItemRequestDto itemRequestDto) {
        return new ItemRequest(
                itemRequestDto.getId(),
                itemRequestDto.getUserId(),
                itemRequestDto.getItemName(),
                itemRequestDto.getItemDescription()
        );
    }
}
