package cn.bupt.driverhailingserver.service;

import cn.bupt.driverhailingserver.entity.Area;

public interface AreaService {
    Area findByDriverId(Long driverId);
}
