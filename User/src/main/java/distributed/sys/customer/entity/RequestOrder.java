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
    private double curX;
    private double curY;
    private double desX;
    private double desY;
    private int serviceLevel;
    private int minDriverLevel;

}
