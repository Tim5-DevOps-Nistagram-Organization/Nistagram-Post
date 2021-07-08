package rs.ac.uns.ftn.devops.tim5.nistagrampost.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestDTO {

    @NotNull(message = "Media can not be null")
    private Long mediaId;

    private String description;

    private Set<TagDTO> tags;

}
