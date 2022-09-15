package ru.practicum.shareit.reviews.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.reviews.model.Review;

import java.util.HashMap;
import java.util.Map;

@Component
public class ReviewStorage implements IReviewStorage {

    private final Map<Long, Review> inMemoryReviewStorage = new HashMap<>();

    private final long id = 0;

}
