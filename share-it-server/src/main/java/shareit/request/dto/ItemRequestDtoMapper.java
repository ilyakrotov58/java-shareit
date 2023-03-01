package shareit.request.dto;

import shareit.item.model.Item;
import shareit.request.model.ItemRequest;

import java.util.ArrayList;

public class ItemRequestDtoMapper {

    public static ItemRequestDto toDto(ItemRequest itemRequest) {

        var itemRequestDtoItems = new ArrayList<ItemRequestDto.Item>();

        for (Item item : itemRequest.getItems()) {
            var itemRequestDtoItem = new ItemRequestDto.Item(
                    item.getId(),
                    item.getName(),
                    item.getDescription(),
                    item.getAvailable(),
                    item.getRequestId()
            );

            itemRequestDtoItems.add(itemRequestDtoItem);
        }

        return new ItemRequestDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getUserId(),
                itemRequest.getCreated(),
                itemRequestDtoItems);
    }

    public static ItemRequest fromDto(ItemRequestDto itemRequestDto) {

        var itemRequestItems = new ArrayList<Item>();

        if (itemRequestDto.getItems() != null) {
            for (ItemRequestDto.Item item : itemRequestDto.getItems()) {
                var itemRequestItem = new Item(
                        item.getId(),
                        0,
                        item.getName(),
                        item.getDescription(),
                        item.getAvailable(),
                        item.getRequestId(),
                        null
                );

                itemRequestItems.add(itemRequestItem);
            }
        }

        return new ItemRequest(
                itemRequestDto.getId(),
                itemRequestDto.getDescription(),
                itemRequestDto.getCreated(),
                itemRequestDto.getUserId(),
                itemRequestItems);
    }
}
