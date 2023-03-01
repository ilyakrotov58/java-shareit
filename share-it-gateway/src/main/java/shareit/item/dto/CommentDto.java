package shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentDto {

    @NotNull
    private long id;

    @NotNull
    private String text;

    @NotNull
    private String authorName;

    @NotNull
    private LocalDateTime createdAt;
}
