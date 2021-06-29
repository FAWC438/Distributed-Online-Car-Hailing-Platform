package distributed.sys.customer.repository;

import distributed.sys.customer.entity.Ordergnls;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface OrderRepository extends CrudRepository<Ordergnls,Long>{
//    Order findById(Long id);
//    List<OrderRepository> findByStartTime(String st);
//    List<OrderRepository> findByEndTime(String ed);
////    List<OrderRepository> findByUserEmail(String userEmail);
//    List<OrderRepository> findByDriverEmail(String driverEmail);
////    List<OrderRepository> findByUserName(String userName);
//    List<OrderRepository> findByDriverName(String driverName);
}
