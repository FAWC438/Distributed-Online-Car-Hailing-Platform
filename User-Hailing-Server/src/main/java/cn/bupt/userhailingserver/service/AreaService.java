package cn.bupt.userhailingserver.service;

import cn.bupt.userhailingserver.entity.Area;

import java.util.List;

public interface AreaService {
    List<Area> findBySectorId(int sectorId);

    Area findByDriverId(Long driverId);
}
