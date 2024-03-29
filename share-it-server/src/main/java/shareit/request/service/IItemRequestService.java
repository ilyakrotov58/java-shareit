package shareit.request.service;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface IItemRequestService {

    ItemRequestDto add(@RequestHeader("X-Sharer-User-Id") long userId,
                       @RequestBody ItemRequestDto itemRequestDto);

    List<ItemRequestDto> get(@RequestHeader("X-Sharer-User-Id") long userId);

    List<ItemRequestDto> getUsersRequests(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam("from") long index,
            @RequestParam("size") long size);

    ItemRequestDto getById(@PathVariable long requestId,
                           @RequestHeader("X-Sharer-User-Id") long userId);

}

