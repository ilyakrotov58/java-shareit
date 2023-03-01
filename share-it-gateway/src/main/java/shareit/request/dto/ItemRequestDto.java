package shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDto {

    private long id;

    @Size(max = 200, message = "Description of item is more than 200 symbols")
    private String description;

    @NotNull
    private long userId;

    @NotNull
    private LocalDateTime created;

    @Nullable
    private List<Item> items;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Item {

        private long id;

        @NotBlank(message = "Item name can't be null or empty")
        private String name;

        @NotBlank(message = "Item description can't be null or empty")
        @Size(max = 200, message = "Description of item is more than 200 symbols")
        private String description;

        @NotNull
        private Boolean available;

        @Nullable
        private Long requestId;
    }
}
