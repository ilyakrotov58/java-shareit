package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class CommentDtoMapper {

    public static CommentDto toDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreatedAt());
    }

    public static Comment fromDto(CommentDto commentDto, User user, Item item) {
        return new Comment(
                commentDto.getId(),
                commentDto.getText(),
                user,
                item,
                commentDto.getCreatedAt());
    }
}
