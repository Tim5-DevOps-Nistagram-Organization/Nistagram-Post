package rs.ac.uns.ftn.devops.tim5.nistagrampost.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReactionUpdateRequestDTO {

    @NotNull(message = "Reaction id  can not be null")
    private Long id;
    @NotNull(message = "Reaction value  can not be null")
    private int reaction;
}
