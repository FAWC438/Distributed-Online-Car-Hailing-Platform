package cn.bupt.userhailingserver.service.impl;

import cn.bupt.userhailingserver.entity.OrderForUser;
import cn.bupt.userhailingserver.repository.OrderForUserRepository;
import cn.bupt.userhailingserver.service.OrderForUserService;
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

    @Override
    @Cacheable(value = "order_id", key = "#p0", condition = "#p0!=null", unless = "#result == null")
    public OrderForUser findById(Long id) {
        return orderForUserRepository.findById(id).orElse(null);
    }

    @Override
    @Cacheable(value = "order_customer_name", key = "#p0", condition = "#p0!=null", unless = "#result == null")
    public List<OrderForUser> findByCustomerName(String customerName) {
        return orderForUserRepository.findByCustomerName(customerName);
    }
}
