package cn.bupt.driverhailingserver.repository;

import cn.bupt.driverhailingserver.entity.Comment;
import cn.bupt.driverhailingserver.entity.Driver;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {
    List<Comment> findByDriverDriverName(Driver driver);

}

