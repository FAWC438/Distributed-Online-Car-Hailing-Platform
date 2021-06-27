package distributed.sys.customer.entity;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Data
@Table(name = "Comment")
public class Comment {
    String content;
    int commentLevel;

    @ManyToOne(cascade = CascadeType.ALL)
    private Driver driver;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "Order")
    private Order order;
}
