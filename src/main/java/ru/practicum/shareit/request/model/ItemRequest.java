package ru.practicum.shareit.request.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * TODO Sprint add-item-requests.
 */

@Data
@AllArgsConstructor
public class ItemRequest {

    private long id;

    @NotNull
    private long userId;

    @NotNull
    private String itemName;

    @Size(max = 200, message = "Description of item is more than 200 symbols")
    private String itemDescription;

}
