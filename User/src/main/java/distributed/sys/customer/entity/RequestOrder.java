package distributed.sys.customer.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class RequestOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

//    private long cId;
    private String customerName;
    private String startTime;
    private String driverName;
    private int priority;
    private int ifCheck;
    private int curX;
    private int curY;
    private int desX;
    private int desY;
    private int serviceLevel;

    @OneToOne(cascade = CascadeType.ALL)
    private Customer customer;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Driver> driver;
//    @JoinTable(name="driver_requestOrder",joinColumns = {@JoinColumn(name = "r_id")},inverseJoinColumns = {@JoinColumn(name = "d_id")})
//    @ManyToMany(cascade = {CascadeType.REFRESH},fetch = FetchType.EAGER)


//    private double curX;
//    private double curY;
//    private double desX;
//    private double desY;

//    private int minDriverLevel;
//    public RequestOrder(){
//        this.customerName = "";
//        this.startTime = "";
//        this.driverName = "";
//        this.priority = -1;
//        this.ifCheck =0;
//        this.curX = -1;
//        this.curY = -1;
//        this.desX = -1;
//        this.desY = -1;
//        this.serviceLevel = 0;
//    }
}
