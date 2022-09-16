package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.reviews.model.Review;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
@AllArgsConstructor
public class User {

    private long id;

    @NotBlank
    @Email(message = "Invalid format of email")
    private String email;

    @NotBlank(message = "First name can't be null")
    private String name;

    @NotBlank(message = "Last name can't be null")
    private String lastName;

    private Set<Review> reviews;
}
