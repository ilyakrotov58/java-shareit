package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.reviews.model.Review;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
@AllArgsConstructor
public class UserDto {

    private long id;

    @NotBlank
    @Email(message = "Invalid format of email")
    private String email;

    private String name;

    private String lastName;

    private Set<Review> reviews;
}
