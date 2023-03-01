package shareit.item.dto;

import shareit.item.model.Comment;
import shareit.item.model.Item;

import java.util.ArrayList;

public class ItemDtoMapper {

    public static ItemDto toDto(Item item) {
        var comments = item.getComments();
        var itemDtoComments = new ArrayList<ItemDto.Comment>();

        for (Comment comment : comments) {
            var itemDtoComment = new ItemDto.Comment(
                    comment.getId(),
                    comment.getText(),
                    comment.getAuthor().getName(),
                    comment.getCreatedAt()
            );

            itemDtoComments.add(itemDtoComment);
        }

        return new ItemDto(
                item.getId(),
                item.getUserId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                null,
                null,
                item.getRequestId(),
                itemDtoComments);
    }

    public static Item fromDto(ItemDto itemDto) {
        Long itemRequestId = null;

        if (itemDto.getRequestId() != null) {
            itemRequestId = itemDto.getRequestId();
        }

        return new Item(
                itemDto.getId(),
                itemDto.getUserId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                itemRequestId,
                new ArrayList<>());
    }
}
