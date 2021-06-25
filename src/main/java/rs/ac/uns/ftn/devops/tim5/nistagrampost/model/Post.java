package rs.ac.uns.ftn.devops.tim5.nistagrampost.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long mediaId;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User user;

    @ManyToMany(cascade = CascadeType.MERGE)
    private Set<Tag> tags;

    public Post(Long mediaId, String description, User user, Set<Tag> tags) {
        this.mediaId = mediaId;
        this.description = description;
        this.user = user;
        this.tags = tags;
    }
}
