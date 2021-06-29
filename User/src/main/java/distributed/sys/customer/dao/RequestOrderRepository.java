package distributed.sys.customer.dao;

import distributed.sys.customer.entity.RequestOrder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RequestOrderRepository extends CrudRepository<RequestOrder,Long>{
    List<OrderRepository> findByStartTime(String st);
    RequestOrder findByCustomerName(String customerName);
}
