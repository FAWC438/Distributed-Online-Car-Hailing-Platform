package cn.bupt.driverhailingserver.repository;

import cn.bupt.driverhailingserver.entity.OrderForUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderForUserRepository extends CrudRepository<OrderForUser, Long> {
//    Order findById(Long id);
//    List<OrderRepository> findByStartTime(String st);
//    List<OrderRepository> findByEndTime(String ed);
////    List<OrderRepository> findByUserEmail(String userEmail);
//    List<OrderRepository> findByDriverEmail(String driverEmail);
////    List<OrderRepository> findByUserName(String userName);
//    List<OrderRepository> findByDriverName(String driverName);
}
