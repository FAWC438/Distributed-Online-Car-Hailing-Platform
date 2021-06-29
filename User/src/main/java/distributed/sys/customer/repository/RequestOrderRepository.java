package distributed.sys.customer.repository;

import distributed.sys.customer.entity.RequestOrder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RequestOrderRepository extends CrudRepository<RequestOrder,Long>{
//    List<OrderRepository> findByStartTime(String st);
    RequestOrder findByCustomerName(String customerName);
}
