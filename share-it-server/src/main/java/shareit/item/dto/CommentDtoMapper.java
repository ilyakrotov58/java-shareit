package shareit.item.dto;

import shareit.item.model.Comment;
import shareit.item.model.Item;
import shareit.user.model.User;

public class CommentDtoMapper {

    public static CommentDto toDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreated());
    }

    public static Comment fromDto(CommentDto commentDto, User user, Item item) {
        return new Comment(
                commentDto.getId(),
                commentDto.getText(),
                user,
                item,
                commentDto.getCreated());
    }
}
