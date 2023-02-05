package ru.practicum.shareit.controllerTests.requests;

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
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoMapper;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.utils.EntityGenerator;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
public class RequestsEndpointsTests {

    @MockBean
    private ItemRequestService requestService;

    @Autowired
    ItemRequestController controller;

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
    void addItemRequest_WithCorrectParams_ReturnsCorrectResponse() throws Exception {

        // Arrange
        var request = EntityGenerator.createRequest();
        var requestDto = ItemRequestDtoMapper.toDto(request);

        Mockito
                .when(requestService.add(Mockito.anyLong(), Mockito.any(ItemRequestDto.class)))
                .thenReturn(requestDto);

        // Act & Assert
        assert requestDto.getItems() != null;

        var firstItem = requestDto.getItems().get(0);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(requestDto))
                        .header("X-Sharer-User-Id", request.getUserId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) requestDto.getId())))
                .andExpect(jsonPath("$.description", is(request.getDescription())))
                .andExpect(jsonPath("$.userId", is(request.getUserId().intValue())))
                .andExpect(jsonPath("$.items[0].id", is((int) firstItem.getId())))
                .andExpect(jsonPath("$.items[0].name", is(firstItem.getName())))
                .andExpect(jsonPath("$.items[0].description", is(firstItem.getDescription())))
                .andExpect(jsonPath("$.items[0].available", is(firstItem.getAvailable())))
                .andExpect(jsonPath("$.items[0].requestId", is(firstItem.getRequestId().intValue())));
    }

    @Test
    void getItemRequest_WithCorrectParams_ReturnsCorrectResponse() throws Exception {

        // Arrange
        var request = EntityGenerator.createRequest();
        var requestDto = ItemRequestDtoMapper.toDto(request);
        var listOfItemRequestsDto = new ArrayList<ItemRequestDto>();
        listOfItemRequestsDto.add(requestDto);

        Mockito
                .when(requestService.get(Mockito.anyLong()))
                .thenReturn(listOfItemRequestsDto);

        // Act & Assert
        assert requestDto.getItems() != null;

        var firstItem = requestDto.getItems().get(0);

        mvc.perform(get("/requests")
                        .content(mapper.writeValueAsString(requestDto))
                        .header("X-Sharer-User-Id", request.getUserId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is((int) requestDto.getId())))
                .andExpect(jsonPath("$.[0].description", is(request.getDescription())))
                .andExpect(jsonPath("$.[0].userId", is(request.getUserId().intValue())))
                .andExpect(jsonPath("$.[0].items[0].id", is((int) firstItem.getId())))
                .andExpect(jsonPath("$.[0].items[0].name", is(firstItem.getName())))
                .andExpect(jsonPath("$.[0].items[0].description", is(firstItem.getDescription())))
                .andExpect(jsonPath("$.[0].items[0].available", is(firstItem.getAvailable())))
                .andExpect(jsonPath("$.[0].items[0].requestId", is(firstItem.getRequestId().intValue())));
    }

    @Test
    void getUsersItemRequest_WithCorrectParams_ReturnsCorrectResponse() throws Exception {

        // Arrange
        var request = EntityGenerator.createRequest();
        var requestDto = ItemRequestDtoMapper.toDto(request);
        var listOfItemRequestsDto = new ArrayList<ItemRequestDto>();
        listOfItemRequestsDto.add(requestDto);

        Mockito
                .when(requestService.getUsersRequests(
                        Mockito.anyLong(),
                        Mockito.anyLong(),
                        Mockito.anyLong()))
                .thenReturn(listOfItemRequestsDto);

        // Act & Assert
        assert requestDto.getItems() != null;

        var firstItem = requestDto.getItems().get(0);

        mvc.perform(get("/requests/all")
                        .content(mapper.writeValueAsString(requestDto))
                        .header("X-Sharer-User-Id", request.getUserId())
                        .param("from", "0")
                        .param("size", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is((int) requestDto.getId())))
                .andExpect(jsonPath("$.[0].description", is(request.getDescription())))
                .andExpect(jsonPath("$.[0].userId", is(request.getUserId().intValue())))
                .andExpect(jsonPath("$.[0].items[0].id", is((int) firstItem.getId())))
                .andExpect(jsonPath("$.[0].items[0].name", is(firstItem.getName())))
                .andExpect(jsonPath("$.[0].items[0].description", is(firstItem.getDescription())))
                .andExpect(jsonPath("$.[0].items[0].available", is(firstItem.getAvailable())))
                .andExpect(jsonPath("$.[0].items[0].requestId", is(firstItem.getRequestId().intValue())));
    }

    @Test
    void getItemRequestById_WithCorrectParams_ReturnsCorrectResponse() throws Exception {

        // Arrange
        var request = EntityGenerator.createRequest();
        var requestDto = ItemRequestDtoMapper.toDto(request);

        Mockito
                .when(requestService.getById(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(requestDto);

        // Act & Assert
        assert requestDto.getItems() != null;

        var firstItem = requestDto.getItems().get(0);

        mvc.perform(get("/requests/" + request.getId())
                        .content(mapper.writeValueAsString(requestDto))
                        .header("X-Sharer-User-Id", request.getUserId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) requestDto.getId())))
                .andExpect(jsonPath("$.description", is(request.getDescription())))
                .andExpect(jsonPath("$.userId", is(request.getUserId().intValue())))
                .andExpect(jsonPath("$.items[0].id", is((int) firstItem.getId())))
                .andExpect(jsonPath("$.items[0].name", is(firstItem.getName())))
                .andExpect(jsonPath("$.items[0].description", is(firstItem.getDescription())))
                .andExpect(jsonPath("$.items[0].available", is(firstItem.getAvailable())))
                .andExpect(jsonPath("$.items[0].requestId", is(firstItem.getRequestId().intValue())));
    }
}
