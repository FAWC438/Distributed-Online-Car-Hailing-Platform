package cn.bupt.userhailingserver.service.impl;

import cn.bupt.userhailingserver.entity.Driver;
import cn.bupt.userhailingserver.repository.DriverRepository;
import cn.bupt.userhailingserver.service.DriverService;
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

    @Override
    public Driver findByCurCustomerName(String customerName) {
        return driverRepository.findByCurCustomerName(customerName);
    }
}
