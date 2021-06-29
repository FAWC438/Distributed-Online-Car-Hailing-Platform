package distributed.sys.customer.repository;

import distributed.sys.customer.entity.OrderForCustomer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface OrderForCustomerRepository extends CrudRepository<OrderForCustomer,Long> {
    //    Order findById(Long id);
//    List<OrderForCustomer> findByStartTime(String st);
//    List<OrderForCustomer> findByEndTime(String ed);
//    //    List<OrderRepository> findByUserEmail(String userEmail);
//    List<OrderForCustomer> findByDriverEmail(String driverEmail);
//    //    List<OrderRepository> findByUserName(String userName);
//    List<OrderForCustomer> findByDriverName(String driverName);

}
