package cn.bupt.userhailingserver.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String content;
    private int commentLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    private Driver driver;

//    @OneToOne(cascade = CascadeType.ALL, mappedBy = "Order")
//    private Order order;

//    public Comment(String content, int commentLevel)
//    {
//        this.commentLevel = commentLevel;
//        this.content = content;
//    }
//
//    public Comment() {
//        this.content = "" ;
//        this.commentLevel = 5;
//    }
}
