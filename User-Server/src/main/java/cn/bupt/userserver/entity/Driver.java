package cn.bupt.userserver.entity;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
public class Driver implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    //    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(name="driver_rOrder",joinColumns = {@JoinColumn(name = "d_id")},inverseJoinColumns = {@JoinColumn(name = "r_id")})
//    @ManyToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
//    private List<RequestOrder> requestOrderList;
//    private List<RequestOrder> requestOrderList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderForUser> orderForUserList;
    //    @JoinColumn(name = "driver_id")
    @OneToMany(cascade = CascadeType.ALL)
    private List<RequestOrderForDriver> requestOrderForDriverList;

//    private List<Order> orderList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "driver_id")
    private List<Comment> commentList;
//    private List<Comment> commentList = new ArrayList<>();

    private int ifLogin;
    //    private Order curOrder;
    private Long curOrderId;
    private String curCustomerName;

    @JsonView(Views.Public.class)
    private String driverName;
    @JsonView(Views.Internal.class)
    private String email;
    @JsonView(Views.Public.class)
    private String password;

    // 司机完成订单次数 完成订单总里程
    @JsonView(Views.Public.class)
    private int finishCount;
    @JsonView(Views.Public.class)
    private int finishDistance;


    @JsonView(Views.Public.class)
    private int serviceLevel;
    @JsonView(Views.Public.class)
    private int driverPoint;
    //    @JsonView(Views.Public.class)
//    private int driverDistance;
    @JsonView(Views.Public.class)
    private int driverLevel;

    @JsonView(Views.Public.class)
    private double stars;
    //    private String driverLevel;

    @JsonView(Views.Internal.class)
    private int ifBusy;
    @JsonView(Views.Internal.class)
    private int curX;
    @JsonView(Views.Internal.class)
    private int curY;
    @JsonView(Views.Internal.class)
    private int desX;
    @JsonView(Views.Internal.class)
    private int desY;

//    @JsonView(Views.Internal.class)
//    private double curX;
//    @JsonView(Views.Internal.class)
//    private double curY;
//    @JsonView(Views.Internal.class)
//    private double desX;
//    @JsonView(Views.Internal.class)
//    private double desY;

//    public Driver()
//    {
//        this.ifLogin = 0;
//        this.curCustomerName = "";
//        this.driverLevel = 0;
//        this.email = "";
//        this.password = "";
//        this.finishCount = 0;
//        this.finishDistance = 0;
//        this.serviceLevel = 0;
//        this.driverPoint =0;
//        this.driverLevel = 0;
//        this.stars = 0;
//        this.ifBusy=  0;
//        this.curX = 25;
//        this.curY = 25;
//        this.desX = 25;
//        this.desY = 25;
////        this.requestOrderList = new ArrayList<RequestOrder>();
////        this.orderList = new ArrayList<Order>();
////        this.commentList = new ArrayList<Comment>();
//    }


}
