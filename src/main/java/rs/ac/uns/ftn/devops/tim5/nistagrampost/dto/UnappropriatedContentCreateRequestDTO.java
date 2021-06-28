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
public class UnappropriatedContentCreateRequestDTO {

    @NotNull(message = "Post can not be null")
    private Long postId;
    @NotNull(message = "You must provide description why is something unappropriated")
    private String description;
}
