package shareit.controllerTests.bookings;

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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import shareit.booking.controller.BookingController;
import shareit.booking.dto.BookingDto;
import shareit.booking.dto.BookingDtoExt;
import shareit.booking.dto.BookingDtoMapper;
import shareit.booking.service.BookingService;
import shareit.utils.EntityGenerator;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
public class BookingEndpointsTests {

    @MockBean
    private BookingService bookingService;

    @Autowired
    BookingController controller;

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
    void addBooking_WithCorrectParams_ReturnsCorrectResponse() throws Exception {

        // Arrange
        var user = EntityGenerator.createUser();
        var item = EntityGenerator.createItem();

        var booking = EntityGenerator.createBooking();
        booking.setBooker(user);
        booking.setItem(item);

        var bookingDtoExt = BookingDtoMapper.toExtDto(booking);

        Mockito
                .when(bookingService.add(Mockito.any(BookingDto.class), Mockito.anyLong()))
                .thenReturn(bookingDtoExt);

        // Act & Assert
        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(BookingDtoMapper.toDto(booking)))
                        .header("X-Sharer-User-Id", user.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) bookingDtoExt.getId())))
                .andExpect(jsonPath("$.booker.id", is((int) bookingDtoExt.getBooker().getId())))
                .andExpect(jsonPath("$.booker.name", is(bookingDtoExt.getBooker().getName())))
                .andExpect(jsonPath("$.booker.email", is(bookingDtoExt.getBooker().getEmail())))
                .andExpect(jsonPath("$.booker.lastName", is(bookingDtoExt.getBooker().getLastName())))
                .andExpect(jsonPath("$.item.id", is((int) bookingDtoExt.getItem().getId())))
                .andExpect(jsonPath("$.item.userId", is((int) bookingDtoExt.getBooker().getId())))
                .andExpect(jsonPath("$.item.name", is(bookingDtoExt.getItem().getName())))
                .andExpect(jsonPath("$.item.description", is(bookingDtoExt.getItem().getDescription())))
                .andExpect(jsonPath("$.item.available", is(bookingDtoExt.getItem().getAvailable())))
                .andExpect(jsonPath("$.item.lastBooking", is(bookingDtoExt.getItem().getLastBooking())))
                .andExpect(jsonPath("$.item.nextBooking", is(bookingDtoExt.getItem().getNextBooking())))
                .andExpect(jsonPath("$.item.comments", is(bookingDtoExt.getItem().getComments())))
                .andExpect(jsonPath("$.status", is(bookingDtoExt.getStatus().name())))
                .andExpect(jsonPath("$.itemId", is((int) bookingDtoExt.getItemId())));
    }

    @Test
    void approveBooking_WithCorrectParams_ReturnsCorrectResponse() throws Exception {

        // Arrange
        var user = EntityGenerator.createUser();
        var item = EntityGenerator.createItem();

        var booking = EntityGenerator.createBooking();
        booking.setBooker(user);
        booking.setItem(item);

        var bookingDtoExt = BookingDtoMapper.toExtDto(booking);

        Mockito
                .when(bookingService.approve(Mockito.anyLong(), Mockito.anyBoolean(), Mockito.anyLong()))
                .thenReturn(bookingDtoExt);

        // Act & Assert
        mvc.perform(patch("/bookings/" + booking.getId())
                        .content(mapper.writeValueAsString(BookingDtoMapper.toDto(booking)))
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", user.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) bookingDtoExt.getId())))
                .andExpect(jsonPath("$.booker.id", is((int) bookingDtoExt.getBooker().getId())))
                .andExpect(jsonPath("$.booker.name", is(bookingDtoExt.getBooker().getName())))
                .andExpect(jsonPath("$.booker.email", is(bookingDtoExt.getBooker().getEmail())))
                .andExpect(jsonPath("$.booker.lastName", is(bookingDtoExt.getBooker().getLastName())))
                .andExpect(jsonPath("$.item.id", is((int) bookingDtoExt.getItem().getId())))
                .andExpect(jsonPath("$.item.userId", is((int) bookingDtoExt.getBooker().getId())))
                .andExpect(jsonPath("$.item.name", is(bookingDtoExt.getItem().getName())))
                .andExpect(jsonPath("$.item.description", is(bookingDtoExt.getItem().getDescription())))
                .andExpect(jsonPath("$.item.available", is(bookingDtoExt.getItem().getAvailable())))
                .andExpect(jsonPath("$.item.lastBooking", is(bookingDtoExt.getItem().getLastBooking())))
                .andExpect(jsonPath("$.item.nextBooking", is(bookingDtoExt.getItem().getNextBooking())))
                .andExpect(jsonPath("$.item.comments", is(bookingDtoExt.getItem().getComments())))
                .andExpect(jsonPath("$.status", is(bookingDtoExt.getStatus().name())))
                .andExpect(jsonPath("$.itemId", is((int) bookingDtoExt.getItemId())));
    }

    @Test
    void getBooking_WithCorrectParams_ReturnsCorrectResponse() throws Exception {

        // Arrange
        var user = EntityGenerator.createUser();
        var item = EntityGenerator.createItem();

        var booking = EntityGenerator.createBooking();
        booking.setBooker(user);
        booking.setItem(item);

        var bookingDtoExt = BookingDtoMapper.toExtDto(booking);

        Mockito
                .when(bookingService.get(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(bookingDtoExt);

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.get("/bookings/" + booking.getId())
                        .header("X-Sharer-User-Id", user.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) bookingDtoExt.getId())))
                .andExpect(jsonPath("$.booker.id", is((int) bookingDtoExt.getBooker().getId())))
                .andExpect(jsonPath("$.booker.name", is(bookingDtoExt.getBooker().getName())))
                .andExpect(jsonPath("$.booker.email", is(bookingDtoExt.getBooker().getEmail())))
                .andExpect(jsonPath("$.booker.lastName", is(bookingDtoExt.getBooker().getLastName())))
                .andExpect(jsonPath("$.item.id", is((int) bookingDtoExt.getItem().getId())))
                .andExpect(jsonPath("$.item.userId", is((int) bookingDtoExt.getBooker().getId())))
                .andExpect(jsonPath("$.item.name", is(bookingDtoExt.getItem().getName())))
                .andExpect(jsonPath("$.item.description", is(bookingDtoExt.getItem().getDescription())))
                .andExpect(jsonPath("$.item.available", is(bookingDtoExt.getItem().getAvailable())))
                .andExpect(jsonPath("$.item.lastBooking", is(bookingDtoExt.getItem().getLastBooking())))
                .andExpect(jsonPath("$.item.nextBooking", is(bookingDtoExt.getItem().getNextBooking())))
                .andExpect(jsonPath("$.item.comments", is(bookingDtoExt.getItem().getComments())))
                .andExpect(jsonPath("$.status", is(bookingDtoExt.getStatus().name())))
                .andExpect(jsonPath("$.itemId", is((int) bookingDtoExt.getItemId())));
    }

    @Test
    void getAllBookings_WithCorrectParams_ReturnsCorrectResponse() throws Exception {

        // Arrange
        var user = EntityGenerator.createUser();
        var item = EntityGenerator.createItem();

        var booking = EntityGenerator.createBooking();
        booking.setBooker(user);
        booking.setItem(item);

        var listOfBookingsDtoExt = new ArrayList<BookingDtoExt>();
        var bookingDtoExt = BookingDtoMapper.toExtDto(booking);
        listOfBookingsDtoExt.add(bookingDtoExt);

        Mockito
                .when(bookingService.getAllBookings(
                        Mockito.anyString(),
                        Mockito.anyLong(),
                        Mockito.anyLong(),
                        Mockito.anyLong()))
                .thenReturn(listOfBookingsDtoExt);

        // Act & Assert
        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", user.getId())
                        .param("from", "0")
                        .param("State", "ALL")
                        .param("size", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is((int) bookingDtoExt.getId())))
                .andExpect(jsonPath("$[0].booker.id", is((int) bookingDtoExt.getBooker().getId())))
                .andExpect(jsonPath("$[0].booker.name", is(bookingDtoExt.getBooker().getName())))
                .andExpect(jsonPath("$[0].booker.email", is(bookingDtoExt.getBooker().getEmail())))
                .andExpect(jsonPath("$[0].booker.lastName", is(bookingDtoExt.getBooker().getLastName())))
                .andExpect(jsonPath("$[0].item.id", is((int) bookingDtoExt.getItem().getId())))
                .andExpect(jsonPath("$[0].item.userId", is((int) bookingDtoExt.getBooker().getId())))
                .andExpect(jsonPath("$[0].item.name", is(bookingDtoExt.getItem().getName())))
                .andExpect(jsonPath("$[0].item.description", is(bookingDtoExt.getItem().getDescription())))
                .andExpect(jsonPath("$[0].item.available", is(bookingDtoExt.getItem().getAvailable())))
                .andExpect(jsonPath("$[0].item.lastBooking", is(bookingDtoExt.getItem().getLastBooking())))
                .andExpect(jsonPath("$[0].item.nextBooking", is(bookingDtoExt.getItem().getNextBooking())))
                .andExpect(jsonPath("$[0].item.comments", is(bookingDtoExt.getItem().getComments())))
                .andExpect(jsonPath("$[0].status", is(bookingDtoExt.getStatus().name())))
                .andExpect(jsonPath("$[0].itemId", is((int) bookingDtoExt.getItemId())));
    }

    @Test
    void getAllBookingsByItems_WithCorrectParams_ReturnsCorrectResponse() throws Exception {

        // Arrange
        var user = EntityGenerator.createUser();
        var item = EntityGenerator.createItem();

        var booking = EntityGenerator.createBooking();
        booking.setBooker(user);
        booking.setItem(item);

        var listOfBookingsDtoExt = new ArrayList<BookingDtoExt>();
        var bookingDtoExt = BookingDtoMapper.toExtDto(booking);
        listOfBookingsDtoExt.add(bookingDtoExt);

        Mockito
                .when(bookingService.getAllBookingsByItems(
                        Mockito.anyString(),
                        Mockito.anyLong(),
                        Mockito.anyLong(),
                        Mockito.anyLong()))
                .thenReturn(listOfBookingsDtoExt);

        // Act & Assert
        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", user.getId())
                        .param("from", "0")
                        .param("State", "ALL")
                        .param("size", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is((int) bookingDtoExt.getId())))
                .andExpect(jsonPath("$[0].booker.id", is((int) bookingDtoExt.getBooker().getId())))
                .andExpect(jsonPath("$[0].booker.name", is(bookingDtoExt.getBooker().getName())))
                .andExpect(jsonPath("$[0].booker.email", is(bookingDtoExt.getBooker().getEmail())))
                .andExpect(jsonPath("$[0].booker.lastName", is(bookingDtoExt.getBooker().getLastName())))
                .andExpect(jsonPath("$[0].item.id", is((int) bookingDtoExt.getItem().getId())))
                .andExpect(jsonPath("$[0].item.userId", is((int) bookingDtoExt.getBooker().getId())))
                .andExpect(jsonPath("$[0].item.name", is(bookingDtoExt.getItem().getName())))
                .andExpect(jsonPath("$[0].item.description", is(bookingDtoExt.getItem().getDescription())))
                .andExpect(jsonPath("$[0].item.available", is(bookingDtoExt.getItem().getAvailable())))
                .andExpect(jsonPath("$[0].item.lastBooking", is(bookingDtoExt.getItem().getLastBooking())))
                .andExpect(jsonPath("$[0].item.nextBooking", is(bookingDtoExt.getItem().getNextBooking())))
                .andExpect(jsonPath("$[0].item.comments", is(bookingDtoExt.getItem().getComments())))
                .andExpect(jsonPath("$[0].status", is(bookingDtoExt.getStatus().name())))
                .andExpect(jsonPath("$[0].itemId", is((int) bookingDtoExt.getItemId())));
    }
}
