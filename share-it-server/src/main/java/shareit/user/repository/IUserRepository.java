package shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shareit.user.model.User;

public interface IUserRepository extends JpaRepository<User, Long> {
}
