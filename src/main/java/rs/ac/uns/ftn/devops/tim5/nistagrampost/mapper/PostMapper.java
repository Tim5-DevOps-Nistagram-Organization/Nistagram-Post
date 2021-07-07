package rs.ac.uns.ftn.devops.tim5.nistagrampost.mapper;

import rs.ac.uns.ftn.devops.tim5.nistagrampost.dto.PostDetailsDTO;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.dto.PostRequestDTO;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.dto.ReactionDTO;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.Post;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.Reaction;

import java.util.stream.Collectors;

public class PostMapper {

    private PostMapper() {
    }

    public static Post toEntity(PostRequestDTO postDTO) {
        return new Post(postDTO.getMediaId(), postDTO.getDescription(),
                postDTO.getTags().stream().map(TagMapper::toEntity).collect(Collectors.toSet()));
    }

    public static PostDetailsDTO toDtoDetails(Post post, Reaction reaction) {
        return new PostDetailsDTO(post.getId(), post.getMediaId(), post.getUser().getUsername(), post.getDescription(),
                reaction == null ? null : new ReactionDTO(reaction.getId(), reaction.getReaction()));
    }
}
