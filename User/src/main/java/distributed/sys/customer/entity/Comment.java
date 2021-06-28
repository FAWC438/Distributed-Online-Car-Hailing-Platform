package distributed.sys.customer.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "Comment")
public class Comment {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    String content;
    int commentLevel;

    @ManyToOne(cascade = CascadeType.ALL)
    private Driver driver;

//    @OneToOne(cascade = CascadeType.ALL, mappedBy = "Order")
//    private Order order;

    public Comment(String content, int commentLevel)
    {
        this.commentLevel = commentLevel;
        this.content = content;
    }
}
