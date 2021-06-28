package distributed.sys.customer.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "Order")
public class Order {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(cascade = CascadeType.ALL)
    private Customer customer;

    @ManyToOne(cascade = CascadeType.ALL)
    private Driver driver;

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "Comment")
//    private Comment comment;

    private String customerName;
    private String driverName;
    private String startTime;
    private String endTime;
    private int serviceLevel;
    private int curX;
    private int curY;
    private int desX;
    private int desY;
//    private double curX;
//    private double curY;
//    private double desX;
//    private double desY;
//    private int useTime;
    private int distance;
    private int price;
    private int curState;// 0未完成 1已完成
//    private int curOrderId;
}
