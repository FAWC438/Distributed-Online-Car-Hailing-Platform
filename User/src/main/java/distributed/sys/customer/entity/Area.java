package distributed.sys.customer.entity;


import lombok.Data;

import javax.persistence.*;


@Data
@Entity
@Table(name = "Area", indexes = {@Index(columnList = "sectorId")})
public class Area {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Id
    private int sectorId;// 51 * 51 的上车点 3*3的九个点为一个区域 一个点最多可以存在四个区域内
    private int driverId;
}


