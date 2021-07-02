package distributed.sys.customer.repository;

import distributed.sys.customer.entity.OrderForUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderForUserRepository extends CrudRepository<OrderForUser,Long>{
//    Order findById(Long id);
//    List<OrderRepository> findByStartTime(String st);
//    List<OrderRepository> findByEndTime(String ed);
////    List<OrderRepository> findByUserEmail(String userEmail);
//    List<OrderRepository> findByDriverEmail(String driverEmail);
////    List<OrderRepository> findByUserName(String userName);
    List<OrderForUser> findByDriverName(String driverName);
    List<OrderForUser> findByCustomerName(String customerName);
}
