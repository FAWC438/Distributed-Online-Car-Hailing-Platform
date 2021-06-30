package cn.bupt.driverserver.repository;

import cn.bupt.driverserver.entity.Comment;
import cn.bupt.driverserver.entity.Driver;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {
    List<Comment> findByDriverDriverName(Driver driver);

}

