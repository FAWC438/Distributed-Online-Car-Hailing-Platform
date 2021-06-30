package cn.bupt.driverhailingserver.service.impl;

import cn.bupt.driverhailingserver.entity.Area;
import cn.bupt.driverhailingserver.repository.AreaRepository;
import cn.bupt.driverhailingserver.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class AreaServiceImpl implements AreaService {
    final AreaRepository areaRepository;

    @Autowired
    public AreaServiceImpl(AreaRepository areaRepository) {
        this.areaRepository = areaRepository;
    }

    // 结果为null或者参数为null时不缓存
    @Override
    @Cacheable(value = "area_driver_id", key = "#p0", condition = "#p0!=null", unless = "#result == null")
    public Area findByDriverId(Long driverId) {
        return areaRepository.findByDriverId(driverId);
    }
}
