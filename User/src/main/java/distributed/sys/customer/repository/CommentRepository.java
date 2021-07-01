package distributed.sys.customer.repository;

import distributed.sys.customer.entity.Comment;
import distributed.sys.customer.entity.Driver;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {
    List<Comment> findByDriverDriverName(Driver driver);

}

