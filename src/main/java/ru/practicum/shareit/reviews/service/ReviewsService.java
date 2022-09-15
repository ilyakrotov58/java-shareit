package ru.practicum.shareit.reviews.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.reviews.storage.IReviewStorage;

@Service
@Slf4j
public class ReviewsService implements IReviewService {

    @Autowired
    private IReviewStorage reviewStorage;
}
