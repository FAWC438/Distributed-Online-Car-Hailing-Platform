package cn.bupt.driverhailingserver.service.impl;

import cn.bupt.driverhailingserver.entity.OrderForUser;
import cn.bupt.driverhailingserver.repository.OrderForUserRepository;
import cn.bupt.driverhailingserver.service.OrderForUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderForUserServiceImpl implements OrderForUserService {

    final OrderForUserRepository orderForUserRepository;

    @Autowired
    public OrderForUserServiceImpl(OrderForUserRepository orderForUserRepository) {
        this.orderForUserRepository = orderForUserRepository;
    }


    //    @Cacheable(value = "order_id", key = "#p0", condition = "#p0!=null", unless = "#result == null")
    @Override
    public OrderForUser findById(Long id) {
        return orderForUserRepository.findById(id).orElse(null);
    }

    @Override
    @Cacheable(value = "order_driver_name", key = "#p0", condition = "#p0!=null", unless = "#result == null")
    public List<OrderForUser> findByDriverName(String driverName) {
        return orderForUserRepository.findByDriverName(driverName);
    }
}
