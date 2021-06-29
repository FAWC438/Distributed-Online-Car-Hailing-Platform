package distributed.sys.customer.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class RequestOrder {
    @Id
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
    private int serviceLevel;

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
