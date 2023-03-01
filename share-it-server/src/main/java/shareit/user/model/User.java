package shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Entity
@Table(name = "users", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @Email(message = "Invalid format of email")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotBlank(message = "First name can't be null")
    @Column(name = "user_name", nullable = false)
    private String name;

    @Column(name = "last_name")
    private String lastName;
}
