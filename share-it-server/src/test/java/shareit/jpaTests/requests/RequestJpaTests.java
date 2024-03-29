package shareit.jpaTests.requests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import shareit.item.model.Item;
import shareit.item.repository.IItemRepository;
import shareit.request.repository.IItemRequestRepository;
import shareit.user.repository.IUserRepository;
import shareit.utils.EntityGenerator;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class RequestJpaTests {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IItemRepository itemRepository;
    @Autowired
    private IItemRequestRepository itemRequestRepository;

    @Test
    void getAllBookings_WithCorrectParams_ReturnsCorrectResponse() {

        // Arrange
        var user = userRepository.save(EntityGenerator.createUser());
        var item = EntityGenerator.createItem();
        item.setUserId(user.getId());
        item.setRequestId(null);

        itemRepository.save(item);
        var request = EntityGenerator.createRequest();
        request.setUserId(user.getId());
        item.setRequestId(null);
        item.setUserId(user.getId());
        var itemList = new ArrayList<Item>();
        itemList.add(item);
        request.setItems(itemList);

        var expectedRequest = itemRequestRepository.save(request);

        // Act
        var actualRequests = itemRequestRepository.getRequests(user.getId());

        // Assert
        assertThat(actualRequests).hasSize(1);

        assertThat(actualRequests.get(0).getId()).isEqualTo(expectedRequest.getId());
    }

    @Test
    void getAllBookingsOtherUsers_WithCorrectParams_ReturnsCorrectResponse() {

        // Arrange
        var user = userRepository.save(EntityGenerator.createUser());
        var item = EntityGenerator.createItem();
        item.setUserId(user.getId());
        item.setRequestId(null);

        itemRepository.save(item);
        var request = EntityGenerator.createRequest();
        request.setUserId(user.getId());
        item.setRequestId(null);
        item.setUserId(user.getId());
        var itemList = new ArrayList<Item>();
        itemList.add(item);
        request.setItems(itemList);

        var expectedRequest = itemRequestRepository.save(request);
        item.setRequestId(expectedRequest.getId());
        itemRepository.save(item);

        // Act
        var actualRequests = itemRequestRepository.getItemRequestsOtherUsers(user.getId() + 1);

        // Assert
        assertThat(
                actualRequests
                        .stream()
                        .anyMatch(r -> r.getId() == expectedRequest.getId()))
                .isTrue();
    }
}
