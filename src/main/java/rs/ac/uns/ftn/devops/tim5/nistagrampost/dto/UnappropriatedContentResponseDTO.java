package rs.ac.uns.ftn.devops.tim5.nistagrampost.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class UnappropriatedContentResponseDTO {

    String requestorUsername;
    String description;
    Long unappropriatedContentId;
    PostResponseDTO postResponseDTO;
}
