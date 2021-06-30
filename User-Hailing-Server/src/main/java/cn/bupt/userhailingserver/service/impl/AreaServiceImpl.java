package cn.bupt.userhailingserver.service.impl;

import cn.bupt.userhailingserver.entity.Area;
import cn.bupt.userhailingserver.service.AreaService;
import cn.bupt.userhailingserver.repository.AreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AreaServiceImpl implements AreaService {
    final AreaRepository areaRepository;

    @Autowired
    public AreaServiceImpl(AreaRepository areaRepository) {
        this.areaRepository = areaRepository;
    }

    @Override
    @Cacheable(value = "area_sector_id", key = "#p0", condition = "#p0!=null", unless = "#result == null")
    public List<Area> findBySectorId(int sectorId) {
        return areaRepository.findBySectorId(sectorId);
    }

    // 结果为null或者参数为null时不缓存
    @Override
    @Cacheable(value = "area_driver_id", key = "#p0", condition = "#p0!=null", unless = "#result == null")
    public Area findByDriverId(Long driverId) {
        return areaRepository.findByDriverId(driverId);
    }
}
