package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ItemDtoMapper {

    public static ItemDto toDto(Item item) {
        var comments = item.getComments();
        List<CommentDto> commentsToDto = new ArrayList<>();
        if (comments != null && comments.size() > 0) {
            commentsToDto = comments
                    .stream()
                    .map(CommentDtoMapper::toDto)
                    .collect(Collectors.toList());
        }
        return new ItemDto(
                item.getId(),
                item.getUserId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                null,
                null,
                commentsToDto);
    }

    public static Item fromDto(ItemDto itemDto) {
        return new Item(
                itemDto.getId(),
                itemDto.getUserId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                new ArrayList<>());
    }
}
