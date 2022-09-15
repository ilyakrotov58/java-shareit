package ru.practicum.shareit.request.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.storage.IItemRequestStorage;

@Service
@Slf4j
public class ItemRequestService {

    @Autowired
    private IItemRequestStorage itemRequestStorage;
}
