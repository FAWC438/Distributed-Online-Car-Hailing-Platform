package cn.bupt.driverhailingserver.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class OrderForCustomer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "Comment")
//    private Comment comment;

    @OneToOne(cascade = CascadeType.ALL)
    private OrderForDriver orderForDriver;

    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

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
//    public Order(){
//        this.customerName = "";
//        this.driverName = "";
//        this.startTime = "";
//        this.endTime = "";
//        this.serviceLevel =0;
//        this.curX = -1;
//        this.curY = -1;
//        this.desX = -1;
//        this.desY = -1;
//    }

}
