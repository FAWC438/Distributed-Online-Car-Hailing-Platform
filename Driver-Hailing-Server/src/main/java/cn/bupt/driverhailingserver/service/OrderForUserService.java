package cn.bupt.driverhailingserver.service;

import cn.bupt.driverhailingserver.entity.OrderForUser;

import java.util.List;

public interface OrderForUserService {
    OrderForUser findById(Long id);

    List<OrderForUser> findByDriverName(String driverName);
}
