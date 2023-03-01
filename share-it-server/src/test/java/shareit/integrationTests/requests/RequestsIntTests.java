package shareit.integrationTests.requests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import shareit.ShareItServerApp;
import shareit.booking.repository.IBookingRepository;
import shareit.item.model.Item;
import shareit.item.repository.ICommentRepository;
import shareit.item.repository.IItemRepository;
import shareit.request.dto.ItemRequestDtoMapper;
import shareit.request.repository.IItemRequestRepository;
import shareit.request.service.IItemRequestService;
import shareit.user.repository.IUserRepository;
import shareit.utils.EntityGenerator;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ShareItServerApp.class)
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
