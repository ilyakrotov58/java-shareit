package ru.practicum.shareit.jsonTests.items;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoMapper;
import ru.practicum.shareit.utils.EntityGenerator;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemsJsonTests {

    @Autowired
    private JacksonTester<ItemDto> json;

    @Test
    void testItemDto() throws Exception {

        // Arrange
        var itemDto = ItemDtoMapper.toDto(EntityGenerator.createItem());
        itemDto.setRequestId(0L);

        // Act
        JsonContent<ItemDto> result = json.write(itemDto);

        // Assert
        assertThat(result).extractingJsonPathNumberValue("$.id")
                .isEqualTo((int) itemDto.getId());

        assertThat(result).extractingJsonPathNumberValue("$.userId")
                .isEqualTo((int) itemDto.getUserId());

        assertThat(result).extractingJsonPathStringValue("$.name")
                .contains(itemDto.getName());

        assertThat(result).extractingJsonPathStringValue("$.description")
                .contains(itemDto.getDescription());

        assertThat(result).extractingJsonPathBooleanValue("$.available")
                .isEqualTo(itemDto.getAvailable());

        assertThat(result).extractingJsonPathValue("$.lastBooking")
                .isEqualTo(itemDto.getLastBooking());

        assertThat(result).extractingJsonPathValue("$.nextBooking")
                .isEqualTo(itemDto.getNextBooking());

        assertThat(result).extractingJsonPathNumberValue("$.requestId")
                .isEqualTo(itemDto.getRequestId().intValue());

        assertThat(result).extractingJsonPathValue("$.comments")
                .isEqualTo(itemDto.getComments());
    }
}
