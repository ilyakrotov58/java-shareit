package shareit.request.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shareit.exceptions.NotFoundException;
import shareit.exceptions.ValidateException;
import shareit.item.repository.IItemRepository;
import shareit.request.dto.ItemRequestDto;
import shareit.request.dto.ItemRequestDtoMapper;
import shareit.request.model.ItemRequest;
import shareit.request.repository.IItemRequestRepository;
import shareit.user.repository.IUserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ItemRequestService implements IItemRequestService {

    @Autowired
    private IItemRequestRepository itemRequestRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IItemRepository itemRepository;

    @Override
    public ItemRequestDto add(long userId, ItemRequestDto itemRequestDto) {

        checkIfUserExists(userId);
        itemRequestDto.setCreated(LocalDateTime.now());

        if (itemRequestDto.getDescription() == null || itemRequestDto.getDescription().isEmpty()) {
            throw new ValidateException("Item request description can't be empty");
        }

        var itemRequest = ItemRequestDtoMapper.fromDto(itemRequestDto);
        itemRequest.setUserId(userId);

        var savedItemRequestDto = itemRequestRepository.save(itemRequest);
        log.info(String.format("Request with id =%s was added", savedItemRequestDto.getId()));

        return ItemRequestDtoMapper.toDto(savedItemRequestDto);
    }

    @Override
    public List<ItemRequestDto> get(long userId) {

        checkIfUserExists(userId);

        var itemsRequests = itemRequestRepository.getRequests(userId);

        setItemsToItemRequests(itemsRequests);

        return itemsRequests
                .stream()
                .map(ItemRequestDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestDto> getUsersRequests(long userId, long index, long size) {

        checkIfUserExists(userId);

        if (size < 1) throw new ValidateException("Size should be > 0");

        if (index < 0) {
            throw new ValidateException("Index should be >= 0");
        }

        var itemsRequestsOtherUsers = itemRequestRepository.getItemRequestsOtherUsers(userId);

        itemsRequestsOtherUsers = itemsRequestsOtherUsers
                .stream()
                .skip(index)
                .limit(size)
                .collect(Collectors.toList());

        setItemsToItemRequests(itemsRequestsOtherUsers);

        return itemsRequestsOtherUsers
                .stream()
                .map(ItemRequestDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestDto getById(long requestId, long userId) {

        checkIfUserExists(userId);

        var request = itemRequestRepository.findById(requestId);

        if (request.isEmpty()) {
            throw new NotFoundException(String.format("Request with id %s not found", requestId));
        }

        var items = itemRepository.getItemsByRequestId(request.get().getId());
        request.get().setItems(items);

        return ItemRequestDtoMapper.toDto(request.get());
    }

    private void checkIfUserExists(long userId) {
        var user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new NotFoundException(String.format("User with id %s can't be found", userId));
        }
    }

    private void setItemsToItemRequests(List<ItemRequest> itemRequests) {

        var items = itemRepository.findAll();

        for (ItemRequest itemRequest : itemRequests) {
            var itemsByRequest = items
                    .stream()
                    .filter(i -> i.getRequestId() != null)
                    .filter(i -> i.getRequestId() == itemRequest.getId())
                    .collect(Collectors.toList());

            itemRequest.setItems(itemsByRequest);
        }
    }
}
