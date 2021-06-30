package cn.bupt.userhailingserver.service;

import cn.bupt.userhailingserver.entity.OrderForUser;

import java.util.List;

public interface OrderForUserService {
    OrderForUser findById(Long id);

    List<OrderForUser> findByCustomerName(String customerName);
}
