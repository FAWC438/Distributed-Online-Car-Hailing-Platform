package cn.bupt.driverserver.repository;

import cn.bupt.driverserver.entity.OrderForDriver;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderForDriverRepository extends CrudRepository<OrderForDriver,Long> {
    //    Order findById(Long id);
//    List<OrderForDriver> findByStartTime(String st);
//    List<OrderForDriver> findByEndTime(String ed);
//    //    List<OrderRepository> findByUserEmail(String userEmail);
////    List<OrderForDriver> findByDriverEmail(String driverEmail);
//    //    List<OrderRepository> findByUserName(String userName);
//    List<OrderForDriver> findByDriverName(String driverName);

}
