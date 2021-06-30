package cn.bupt.userhailingserver.service.impl;

import cn.bupt.userhailingserver.entity.Customer;
import cn.bupt.userhailingserver.repository.CustomerRepository;
import cn.bupt.userhailingserver.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {

    final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    @Cacheable(value = "customer_name", key = "#p0", condition = "#p0!=null", unless = "#result == null")
    public Customer findByCustomerName(String CustomerName) {
        return customerRepository.findByCustomerName(CustomerName);
    }
}
