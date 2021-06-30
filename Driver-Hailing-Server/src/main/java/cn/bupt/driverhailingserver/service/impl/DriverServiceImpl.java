package cn.bupt.driverhailingserver.service.impl;

import cn.bupt.driverhailingserver.entity.Driver;
import cn.bupt.driverhailingserver.repository.CustomerRepository;
import cn.bupt.driverhailingserver.repository.DriverRepository;
import cn.bupt.driverhailingserver.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class DriverServiceImpl implements DriverService {

    final DriverRepository driverRepository;

    @Autowired
    public DriverServiceImpl(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    @Override
    @Cacheable(value = "driver_name", key = "#p0", condition = "#p0!=null", unless = "#result == null")
    public Driver findByDriverName(String driverName) {
        return driverRepository.findByDriverName(driverName);
    }
}
