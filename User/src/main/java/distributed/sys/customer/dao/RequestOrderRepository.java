package distributed.sys.customer.dao;

import distributed.sys.customer.entity.RequestOrder;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RequestOrderRepository extends CrudRepository<RequestOrder,Long>{
    List<OrderRepository> findByStartTime(String st);
    RequestOrder findByCustomerName(String customerName);

}
