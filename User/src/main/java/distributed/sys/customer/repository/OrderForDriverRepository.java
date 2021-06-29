package distributed.sys.customer.repository;

import distributed.sys.customer.entity.OrderForCustomer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface OrderForDriverRepository extends CrudRepository<OrderForCustomer,Long> {
    //    Order findById(Long id);
//    List<OrderForDriver> findByStartTime(String st);
//    List<OrderForDriver> findByEndTime(String ed);
//    //    List<OrderRepository> findByUserEmail(String userEmail);
////    List<OrderForDriver> findByDriverEmail(String driverEmail);
//    //    List<OrderRepository> findByUserName(String userName);
//    List<OrderForDriver> findByDriverName(String driverName);

}
