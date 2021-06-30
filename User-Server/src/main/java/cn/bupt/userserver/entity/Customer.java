package cn.bupt.userserver.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(cascade = CascadeType.ALL)
    private RequestOrder requestOrder;

    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderForUser> orderForUserList;
//    @JoinColumn(name = "customer_id")

//    private List<Order> orderList = new ArrayList<>();

    private int ifLogin;
    private String customerName;
    private String email;
    private String password;

    // 用户乘坐里程数 和 用户乘坐次数
    private int takeCount;
    private int takeDistance;

    private int membershipPoint;
    //    private String membershipLevel;
    private int membershipLevel;

    private int curX;
    private int curY;

//    @JsonView(Views.Internal.class)
//    private double curX;
//    @JsonView(Views.Internal.class)
//    private double curY;
//    @JsonView(Views.Internal.class)
//    private double desX;
//    @JsonView(Views.Internal.class)
//    private double desY;





//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "Order")



//    public Customer()
//    {
//        this.ifLogin = 0;
//        this.password = "";
//        this.email = "";
//        this.curX = -1;
//        this.curY = -1;
//        this.membershipLevel = 0;
//        this.membershipPoint = 0;
//        this.CustomerName = "";
//        this.takeCount = 0;
//        this.takeDistance = 0;
////        this.requestOrder = new RequestOrder();
////        this.orderList = new ArrayList<Order>();
//    }

}
