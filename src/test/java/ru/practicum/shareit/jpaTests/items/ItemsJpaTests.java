package ru.practicum.shareit.jpaTests.items;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.IItemRepository;
import ru.practicum.shareit.request.repository.IItemRequestRepository;
import ru.practicum.shareit.user.repository.IUserRepository;
import ru.practicum.shareit.utils.EntityGenerator;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ItemsJpaTests {

    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IItemRepository itemRepository;
    @Autowired
    private IItemRequestRepository itemRequestRepository;

    @Test
    void getAllItems_WithCorrectParams_ReturnCorrectResponse() {

        // Arrange
        var user = userRepository.save(EntityGenerator.createUser());
        var item = EntityGenerator.createItem();
        item.setRequestId(null);
        item.setUserId(user.getId());
        var itemInDb = itemRepository.save(item);

        // Act
        var actualItems = itemRepository.getAll(item.getUserId());

        // Assert
        assertThat(actualItems.get(0).getId()).isEqualTo(itemInDb.getId());
    }

    @Test
    void getAllItemsByRequestId_WithCorrectParams_ReturnCorrectResponse() {

        // Arrange
        var user = userRepository.save(EntityGenerator.createUser());
        var item = EntityGenerator.createItem();
        item.setRequestId(null);
        item.setUserId(user.getId());
        var itemInDb = itemRepository.save(item);

        var itemsList = new ArrayList<Item>();
        itemsList.add(itemInDb);

        var request = EntityGenerator.createRequest();
        request.setItems(itemsList);
        request.setUserId(user.getId());

        var requestInDb = itemRequestRepository.save(request);

        itemInDb.setRequestId(requestInDb.getId());
        itemRepository.save(itemInDb);

        // Act
        var actualItems = itemRepository.getItemsByRequestId(requestInDb.getId());

        // Assert
        assertThat(actualItems.get(0).getId()).isEqualTo(itemInDb.getId());
    }
}
