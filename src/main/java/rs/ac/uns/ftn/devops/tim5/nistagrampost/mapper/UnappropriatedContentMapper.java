package rs.ac.uns.ftn.devops.tim5.nistagrampost.mapper;

import rs.ac.uns.ftn.devops.tim5.nistagrampost.dto.UnappropriatedContentCreateRequestDTO;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.dto.UnappropriatedContentResponseDTO;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.UnappropriatedContent;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.enums.UnappropriatedContentState;

public class UnappropriatedContentMapper {

    private UnappropriatedContentMapper() {
        throw new IllegalStateException("UnappropriatedContentMapper class");
    }

    public static UnappropriatedContent newToEntity(UnappropriatedContentCreateRequestDTO requestDTO) {
        return new UnappropriatedContent(null, UnappropriatedContentState.REQUESTED,
                requestDTO.getDescription(), null, requestDTO.getPostId(), null);

    }

    public static UnappropriatedContentResponseDTO toDto(UnappropriatedContent unappropriatedContent) {
        return new UnappropriatedContentResponseDTO(
                unappropriatedContent.getId(),
                unappropriatedContent.getInitiator().getUsername(),
                unappropriatedContent.getDescription(),
                unappropriatedContent.getPost().getMediaId());
    }

}
