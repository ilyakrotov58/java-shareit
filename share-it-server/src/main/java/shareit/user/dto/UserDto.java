package shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private long id;

    @NotBlank
    @Email(message = "Invalid format of email")
    private String email;

    private String name;

    private String lastName;
}
