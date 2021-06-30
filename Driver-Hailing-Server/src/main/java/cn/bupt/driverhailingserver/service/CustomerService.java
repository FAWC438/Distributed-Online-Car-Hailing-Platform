package cn.bupt.driverhailingserver.service;

import cn.bupt.driverhailingserver.entity.Customer;

public interface CustomerService {
    Customer findByCustomerName(String CustomerName);
}
