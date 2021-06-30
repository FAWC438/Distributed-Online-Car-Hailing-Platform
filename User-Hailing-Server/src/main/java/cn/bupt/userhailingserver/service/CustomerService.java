package cn.bupt.userhailingserver.service;

import cn.bupt.userhailingserver.entity.Customer;

public interface CustomerService {
    Customer findByCustomerName(String CustomerName);
}
