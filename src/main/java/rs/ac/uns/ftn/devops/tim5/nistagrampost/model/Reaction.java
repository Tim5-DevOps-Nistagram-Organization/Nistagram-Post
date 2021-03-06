package rs.ac.uns.ftn.devops.tim5.nistagrampost.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.enums.ReactionEnum;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Reaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(value = EnumType.ORDINAL)
    private ReactionEnum reaction;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private Post post;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private User user;


    public Reaction(ReactionEnum reaction, Post post) {
        this.id = null;
        this.user = null;
        this.reaction = reaction;
        this.post = post;
    }

    public Reaction(Long id) {
        this.id = id;
        this.user = new User();
        this.reaction = ReactionEnum.LIKE;
        this.post = new Post();
    }

    public Reaction(Long id, ReactionEnum reaction) {
        this.id = id;
        this.user = new User();
        this.reaction = reaction;
        this.post = new Post();
    }

}
