package distributed.sys.customer.repository;

import distributed.sys.customer.entity.RequestOrderForDriver;
import org.springframework.data.repository.CrudRepository;

public interface RequestOrderForDriverRepository extends CrudRepository<RequestOrderForDriver, Long> {
//    List<RequestOrderForDriver> findByDriverName(String driverName);
}
