package ru.practicum.shareit.controllerTests.items;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoMapper;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.utils.DataGenerator;
import ru.practicum.shareit.utils.EntityGenerator;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
public class ItemsEndpointsTests {

    @MockBean
    private ItemService itemService;

    @Autowired
    ItemController controller;

    @Autowired
    private MockMvc mvc;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {

        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    void getItemById_WithCorrectParams_ReturnsCorrectResponse() throws Exception {

        // Arrange
        var item = EntityGenerator.createItem();
        var itemDto = ItemDtoMapper.toDto(item);

        Mockito
                .when(itemService.getById(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(itemDto);

        // Act & Assert
        mvc.perform(get("/items/" + item.getId())
                        .content(mapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", item.getUserId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) itemDto.getId())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.userId", is((int) itemDto.getUserId())))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.requestId", is(itemDto.getRequestId() == null
                        ? 0
                        : itemDto.getRequestId().intValue())))
                .andExpect(jsonPath("$.nextBooking", is(itemDto.getNextBooking())))
                .andExpect(jsonPath("$.lastBooking", is(itemDto.getLastBooking())))
                .andExpect(jsonPath("$.comments", is(itemDto.getComments())));
    }

    @Test
    void addItem_WithCorrectParams_ReturnsCorrectResponse() throws Exception {

        // Arrange
        var item = EntityGenerator.createItem();
        var itemDto = ItemDtoMapper.toDto(item);

        Mockito
                .when(itemService.add(Mockito.any(ItemDto.class), Mockito.anyLong()))
                .thenReturn(itemDto);

        // Act & Assert
        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", item.getUserId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) itemDto.getId())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.userId", is((int) itemDto.getUserId())))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.requestId", is(itemDto.getRequestId() == null
                        ? 0
                        : itemDto.getRequestId().intValue())))
                .andExpect(jsonPath("$.nextBooking", is(itemDto.getNextBooking())))
                .andExpect(jsonPath("$.lastBooking", is(itemDto.getLastBooking())))
                .andExpect(jsonPath("$.comments", is(itemDto.getComments())));
    }

    @Test
    void deleteItem_WithCorrectParams_ReturnsCorrectResponse() throws Exception {

        // Arrange
        var item = EntityGenerator.createItem();
        var itemDto = ItemDtoMapper.toDto(item);

        // Act & Assert
        mvc.perform(delete("/items/" + item.getId())
                        .content(mapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", item.getUserId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void editItem_WithCorrectParams_ReturnsCorrectResponse() throws Exception {

        // Arrange
        var newItem = EntityGenerator.createItem();
        var newItemDto = ItemDtoMapper.toDto(newItem);

        Mockito
                .when(itemService.edit(
                        Mockito.any(ItemDto.class),
                        Mockito.anyLong(),
                        Mockito.anyLong()))
                .thenReturn(newItemDto);

        // Act & Assert
        mvc.perform(patch("/items/" + newItem.getId())
                        .content(mapper.writeValueAsString(newItemDto))
                        .header("X-Sharer-User-Id", newItem.getUserId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) newItemDto.getId())))
                .andExpect(jsonPath("$.description", is(newItemDto.getDescription())))
                .andExpect(jsonPath("$.userId", is((int) newItemDto.getUserId())))
                .andExpect(jsonPath("$.name", is(newItemDto.getName())))
                .andExpect(jsonPath("$.available", is(newItemDto.getAvailable())))
                .andExpect(jsonPath("$.requestId", is(newItemDto.getRequestId() == null
                        ? 0
                        : newItemDto.getRequestId().intValue())))
                .andExpect(jsonPath("$.nextBooking", is(newItemDto.getNextBooking())))
                .andExpect(jsonPath("$.lastBooking", is(newItemDto.getLastBooking())))
                .andExpect(jsonPath("$.comments", is(newItemDto.getComments())));
    }

    @Test
    void getAllItemsByText_WithCorrectParams_ReturnsCorrectResponse() throws Exception {

        // Arrange
        var item = EntityGenerator.createItem();
        var itemDto = ItemDtoMapper.toDto(item);
        var listItemDto = new ArrayList<ItemDto>();
        listItemDto.add(itemDto);

        Mockito
                .when(itemService.getAllByText(
                        Mockito.anyString(),
                        Mockito.anyLong(),
                        Mockito.anyLong()))
                .thenReturn(listItemDto);

        // Act & Assert
        mvc.perform(get("/items/search")
                        .param("text", DataGenerator.generateRandomString(5))
                        .content(mapper.writeValueAsString(listItemDto))
                        .header("X-Sharer-User-Id", item.getUserId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is((int) itemDto.getId())))
                .andExpect(jsonPath("$.[0].description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.[0].userId", is((int) itemDto.getUserId())))
                .andExpect(jsonPath("$.[0].name", is(itemDto.getName())))
                .andExpect(jsonPath("$.[0].available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.[0].requestId", is(itemDto.getRequestId() == null
                        ? 0
                        : itemDto.getRequestId().intValue())))
                .andExpect(jsonPath("$.[0].nextBooking", is(itemDto.getNextBooking())))
                .andExpect(jsonPath("$.[0].lastBooking", is(itemDto.getLastBooking())))
                .andExpect(jsonPath("$.[0].comments", is(itemDto.getComments())));
    }

    @Test
    void getAllItems_WithCorrectParams_ReturnsCorrectResponse() throws Exception {

        // Arrange
        var item = EntityGenerator.createItem();
        var itemDto = ItemDtoMapper.toDto(item);
        var listItemDto = new ArrayList<ItemDto>();
        listItemDto.add(itemDto);

        Mockito
                .when(itemService.getAll(
                        Mockito.anyLong(),
                        Mockito.anyLong(),
                        Mockito.anyLong()))
                .thenReturn(listItemDto);

        // Act & Assert
        mvc.perform(get("/items")
                        .param("size", "1")
                        .param("index", "0")
                        .content(mapper.writeValueAsString(listItemDto))
                        .header("X-Sharer-User-Id", item.getUserId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is((int) itemDto.getId())))
                .andExpect(jsonPath("$.[0].description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.[0].userId", is((int) itemDto.getUserId())))
                .andExpect(jsonPath("$.[0].name", is(itemDto.getName())))
                .andExpect(jsonPath("$.[0].available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.[0].requestId", is(itemDto.getRequestId() == null
                        ? 0
                        : itemDto.getRequestId().intValue())))
                .andExpect(jsonPath("$.[0].nextBooking", is(itemDto.getNextBooking())))
                .andExpect(jsonPath("$.[0].lastBooking", is(itemDto.getLastBooking())))
                .andExpect(jsonPath("$.[0].comments", is(itemDto.getComments())));
    }

    @Test
    void addComment_WithCorrectParams_ReturnsCorrectResponse() throws Exception {

        // Arrange
        var comment = EntityGenerator.createComment();
        var commentDto = CommentDtoMapper.toDto(comment);

        Mockito
                .when(itemService.addComment(
                        Mockito.anyLong(),
                        Mockito.anyLong(),
                        Mockito.any(CommentDto.class)))
                .thenReturn(commentDto);

        // Act & Assert
        mvc.perform(post("/items/" + comment.getItem().getId() + "/comment")
                        .content(mapper.writeValueAsString(commentDto))
                        .header("X-Sharer-User-Id", comment.getAuthor().getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) commentDto.getId())))
                .andExpect(jsonPath("$.text", is(commentDto.getText())))
                .andExpect(jsonPath("$.authorName", is(commentDto.getAuthorName())));
    }
}
