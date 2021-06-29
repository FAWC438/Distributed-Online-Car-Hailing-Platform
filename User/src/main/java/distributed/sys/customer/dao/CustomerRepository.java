package distributed.sys.customer.dao;

import distributed.sys.customer.entity.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CustomerRepository extends CrudRepository<Customer,Long>{
    Customer findByCustomerName(String CustomerName);
    Customer findByEmail(String email);
    List<CustomerRepository> findByTakeCount(int takeCount);
    List<CustomerRepository> findByTakeDistance(double takeDistance);
    List<CustomerRepository> findByMembershipPoint(int membershipPoint);
    List<CustomerRepository> findByMembershipLevel(int membershipLevel);

}
