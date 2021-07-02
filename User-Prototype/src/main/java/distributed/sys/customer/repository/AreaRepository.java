package distributed.sys.customer.repository;

import distributed.sys.customer.entity.Area;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AreaRepository extends CrudRepository<Area,Long> {
    List<Area> findBySectorId(int sectorId);
    Area findByDriverId(Long driverId);
//    List<Area>
}

