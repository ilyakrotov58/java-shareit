package ru.practicum.shareit.integrationTests.requests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.ShareItApp;
import ru.practicum.shareit.booking.repository.IBookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ICommentRepository;
import ru.practicum.shareit.item.repository.IItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDtoMapper;
import ru.practicum.shareit.request.repository.IItemRequestRepository;
import ru.practicum.shareit.request.service.IItemRequestService;
import ru.practicum.shareit.user.repository.IUserRepository;
import ru.practicum.shareit.utils.EntityGenerator;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ShareItApp.class)
public class RequestsIntTests {

    @Autowired
    IBookingRepository bookingRepository;

    @Autowired
    IUserRepository userRepository;

    @Autowired
    ICommentRepository commentRepository;

    @Autowired
    IItemRequestRepository requestRepository;

    @Autowired
    IItemRepository itemRepository;
    @Autowired
    IItemRequestRepository itemRequestRepository;

    @Autowired
    IItemRequestService itemRequestService;

    @BeforeEach
    void clearDbBeforeTest() {
        commentRepository.deleteAll();
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
        requestRepository.deleteAll();
    }

    @Test
    void getRequest_WithCorrectParams_ReturnsCorrectResponse() {

        // Arrange
        var user = EntityGenerator.createUser();
        var userInDb = userRepository.save(user);

        var item = EntityGenerator.createItem();
        item.setUserId(userInDb.getId());
        item.setRequestId(null);
        item.setComments(null);

        var itemInDb = itemRepository.save(item);

        var request = EntityGenerator.createRequest();
        request.setUserId(userInDb.getId());

        var itemsList = new ArrayList<Item>();
        itemsList.add(itemInDb);
        request.setItems(itemsList);

        var requestInDb = itemRequestRepository.save(request);
        itemInDb.setRequestId(requestInDb.getId());
        itemRepository.save(itemInDb);

        requestInDb.setItems(itemsList);

        var requestDto = ItemRequestDtoMapper.toDto(requestInDb);

        // Act
        var actualRequest =
                itemRequestService.getById(request.getId(), user.getId());

        // Assert
        assertThat(actualRequest)
                .hasFieldOrPropertyWithValue("id", requestDto.getId())
                .hasFieldOrPropertyWithValue("description", requestDto.getDescription())
                .hasFieldOrPropertyWithValue("userId", requestDto.getUserId());
    }

    @AfterEach
    void clearDb() {
        commentRepository.deleteAll();
        itemRepository.deleteAll();
        bookingRepository.deleteAll();
        userRepository.deleteAll();
        requestRepository.deleteAll();
    }
}
