package distributed.sys.customer.dao;

import distributed.sys.customer.entity.Comment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {
//    List<Comment> findByDriverDriverName(Driver driver);

}

