package distributed.sys.customer.entity;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Table(name = "Customer")
public class Customer {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonView(Views.Public.class)
    private String CustomerName;
    @JsonView(Views.Internal.class)
    private String email;
    @JsonView(Views.Internal.class)
    private String password;

    // 用户乘坐里程数 和 用户乘坐次数
    @JsonView(Views.Public.class)
    private int takeCount;
    @JsonView(Views.Public.class)
    private double takeDistance;

    @JsonView(Views.Public.class)
    private int membershipPoint;
    //    private String membershipLevel;
    @JsonView(Views.Public.class)
    private int membershipLevel;

    @JsonView(Views.Internal.class)
    private int curX;
    @JsonView(Views.Internal.class)
    private int curY;

//    @JsonView(Views.Internal.class)
//    private double curX;
//    @JsonView(Views.Internal.class)
//    private double curY;
//    @JsonView(Views.Internal.class)
//    private double desX;
//    @JsonView(Views.Internal.class)
//    private double desY;



    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "RequestOrder")
    private RequestOrder requestOrder;

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "Order")

    @OneToMany(cascade = CascadeType.ALL)
    private List<Order> orderList;

}
