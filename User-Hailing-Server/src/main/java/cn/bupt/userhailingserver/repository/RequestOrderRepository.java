package cn.bupt.userhailingserver.repository;

import cn.bupt.userhailingserver.entity.RequestOrder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RequestOrderRepository extends CrudRepository<RequestOrder, Long> {
    //    List<OrderRepository> findByStartTime(String st);
    RequestOrder findByCustomerName(String customerName);

    RequestOrder findByCustomerId(Long customerId);
}
