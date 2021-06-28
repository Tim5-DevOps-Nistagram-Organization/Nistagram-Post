package rs.ac.uns.ftn.devops.tim5.nistagrampost.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.enums.UnapropriatedContentState;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UnappropriatedContent {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false)
        @Enumerated(value = EnumType.ORDINAL)
        private UnapropriatedContentState state;

        @Column(nullable = false)
        private String description;

        // Because is imposible to delete Post if connected UnappropriatedContent exist
        // we want first to delete Post then execute saga for Post delete
        // and then change state of UnappropriatedContent
        // this is also reason why we have postId filed
        @Transient
        private Post post;

        @Column(nullable = false)
        private Long postId;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn
        private User initiator;
}
