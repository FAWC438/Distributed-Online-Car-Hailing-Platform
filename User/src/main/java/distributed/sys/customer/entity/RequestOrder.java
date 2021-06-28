package distributed.sys.customer.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "RequestOrder")
public class RequestOrder {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "Customer")
    private Customer customer;

    @ManyToOne(cascade = CascadeType.ALL)
    private Driver driver;

    private String customerName;
    private String startTime;
    private String driverName;
    private int priority;
    private int ifCheck;
    private int curX;
    private int curY;
    private int desX;
    private int desY;
//    private double curX;
//    private double curY;
//    private double desX;
//    private double desY;
    private int serviceLevel;
//    private int minDriverLevel;

}
