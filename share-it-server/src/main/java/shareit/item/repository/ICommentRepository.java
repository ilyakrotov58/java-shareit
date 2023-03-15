package shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shareit.item.model.Comment;

public interface ICommentRepository extends JpaRepository<Comment, Long> {
}
