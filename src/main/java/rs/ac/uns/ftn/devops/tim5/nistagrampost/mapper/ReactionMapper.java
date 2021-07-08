package rs.ac.uns.ftn.devops.tim5.nistagrampost.mapper;

import rs.ac.uns.ftn.devops.tim5.nistagrampost.dto.ReactionCreateRequestDTO;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.dto.ReactionUpdateRequestDTO;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.Post;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.Reaction;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.enums.ReactionEnum;

public class ReactionMapper {

    private ReactionMapper() {
    }

    public static Reaction newToEntity(ReactionCreateRequestDTO requestDTO) {
        Post post = new Post();
        post.setId(requestDTO.getPostId());
        return new Reaction(ReactionEnum.of(requestDTO.getReaction()), post);

    }

    public static Reaction updateToEntity(ReactionUpdateRequestDTO requestDTO) {
        return new Reaction(requestDTO.getId(), ReactionEnum.of(requestDTO.getReaction()));

    }

}
