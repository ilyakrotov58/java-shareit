package ru.practicum.shareit.jsonTests.requests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoMapper;
import ru.practicum.shareit.utils.EntityGenerator;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class RequestsJsonTests {

    @Autowired
    private JacksonTester<ItemRequestDto> json;

    @Test
    void testItemDto() throws Exception {

        // Arrange
        var requestDto = ItemRequestDtoMapper.toDto(EntityGenerator.createRequest());

        // Act
        JsonContent<ItemRequestDto> result = json.write(requestDto);

        // Assert
        assertThat(result).extractingJsonPathNumberValue("$.id")
                .isEqualTo((int) requestDto.getId());

        assertThat(result).extractingJsonPathNumberValue("$.userId")
                .isEqualTo((int) requestDto.getUserId());

        assertThat(result).extractingJsonPathStringValue("$.description")
                .contains(requestDto.getDescription());

        assertThat(requestDto.getItems()).hasSize(1);
        var item = requestDto.getItems().get(0);

        assertThat(result).extractingJsonPathNumberValue("$.items[0].id")
                .isEqualTo((int) item.getId());

        assertThat(result).extractingJsonPathNumberValue("$.items[0].userId")
                .isEqualTo((int) item.getUserId());

        assertThat(result).extractingJsonPathStringValue("$.items[0].name")
                .isEqualTo(item.getName());

        assertThat(result).extractingJsonPathStringValue("$.items[0].description")
                .isEqualTo(item.getDescription());

        assertThat(result).extractingJsonPathBooleanValue("$.items[0].available")
                .isEqualTo(item.getAvailable());

        assertThat(result).extractingJsonPathNumberValue("$.items[0].requestId")
                .isEqualTo(item.getRequestId().intValue());

        assertThat(result).extractingJsonPathValue("$.items[0].comments").isNull();
    }
}
