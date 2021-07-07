package rs.ac.uns.ftn.devops.tim5.nistagrampost.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDetailsDTO {
    private Long id;
    private Long mediaId;
    private String username;
    private String description;
    private ReactionDTO reaction;
}
