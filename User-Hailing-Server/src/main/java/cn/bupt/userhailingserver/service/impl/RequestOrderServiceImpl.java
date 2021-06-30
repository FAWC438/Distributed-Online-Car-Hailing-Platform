package cn.bupt.userhailingserver.service.impl;

import cn.bupt.userhailingserver.entity.RequestOrder;
import cn.bupt.userhailingserver.repository.OrderForUserRepository;
import cn.bupt.userhailingserver.repository.RequestOrderRepository;
import cn.bupt.userhailingserver.service.RequestOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class RequestOrderServiceImpl implements RequestOrderService {

    final RequestOrderRepository requestOrderRepository;

    @Autowired
    public RequestOrderServiceImpl(RequestOrderRepository requestOrderRepository) {
        this.requestOrderRepository = requestOrderRepository;
    }

    @Override
    @Cacheable(value = "request_customer_name", key = "#p0", condition = "#p0!=null", unless = "#result == null")
    public RequestOrder findByCustomerName(String customerName) {
        return requestOrderRepository.findByCustomerName(customerName);
    }
}
