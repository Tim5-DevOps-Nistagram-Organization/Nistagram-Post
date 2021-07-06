package rs.ac.uns.ftn.devops.tim5.nistagrampost.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private User writer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private Post post;

    @Transient
    private Long postId;

    @Temporal(TemporalType.DATE)
    private Date date;

    @Column(nullable = false)
    private String message;

    public Comment(Long postId, String message) {
        this.id = null;
        this.writer = null;
        this.postId = postId;
        this.date = new Date();
        this.message = message;
    }

}
