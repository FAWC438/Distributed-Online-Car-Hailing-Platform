package cn.bupt.userhailingserver.service;

import cn.bupt.userhailingserver.entity.Driver;

public interface DriverService {
    Driver findByDriverName(String driverName);

    Driver findByCurCustomerName(String customerName);
}
