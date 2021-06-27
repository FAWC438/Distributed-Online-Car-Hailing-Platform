package distributed.sys.customer.dao;

import distributed.sys.customer.entity.Order;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order,Long>{
    List<OrderRepository> findByStartTime(String st);
    List<OrderRepository> findByEndTime(String ed);
    List<OrderRepository> findByUserEmail(String userEmail);
    List<OrderRepository> findByDriverEmail(String driverEmail);
    List<OrderRepository> findByUserName(String userName);
    List<OrderRepository> findByDriverName(String driverName);

}
