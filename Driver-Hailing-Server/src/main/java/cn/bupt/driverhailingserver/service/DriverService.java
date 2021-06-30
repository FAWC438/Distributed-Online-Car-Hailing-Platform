package cn.bupt.driverhailingserver.service;

import cn.bupt.driverhailingserver.entity.Driver;

public interface DriverService {
    Driver findByDriverName(String driverName);
}
