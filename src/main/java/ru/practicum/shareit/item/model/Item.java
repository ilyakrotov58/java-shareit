package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * TODO Sprint add-controllers.
 */

@Data
@AllArgsConstructor
public class Item {

    private long id;

    @NotNull
    private long userId;

    @NotBlank(message = "Item name can't be null or empty")
    private String name;

    @NotBlank(message = "Item description can't be null or empty")
    @Size(max = 200, message = "Description of item is more than 200 symbols")
    private String description;

    @NotNull()
    private Boolean available;
}
