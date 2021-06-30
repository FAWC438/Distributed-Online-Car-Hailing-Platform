package cn.bupt.userserver.repository;

import cn.bupt.userserver.entity.Area;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AreaRepository extends CrudRepository<Area,Long> {
    List<Area> findBySectorId(int sectorId);
    Area findByDriverId(Long driverId);
//    List<Area>
}

