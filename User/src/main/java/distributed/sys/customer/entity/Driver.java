package distributed.sys.customer.entity;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Table(name = "Driver")
public class Driver {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "RequestOrder")
    private List<RequestOrder> requestOrderList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "Order")
    private List<Order> orderList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "Comment")
    private List<Comment> commentList;

    private int ifLogin;
    private Order curOrder;
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
}
