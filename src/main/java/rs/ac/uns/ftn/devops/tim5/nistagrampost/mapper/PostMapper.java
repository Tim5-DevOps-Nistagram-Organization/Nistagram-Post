package rs.ac.uns.ftn.devops.tim5.nistagrampost.mapper;

import rs.ac.uns.ftn.devops.tim5.nistagrampost.dto.PostRequestDTO;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.dto.PostResponseDTO;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.Post;

import java.util.stream.Collectors;

public class PostMapper {

    public static Post toEntity(PostRequestDTO postDTO) {
        return new Post(postDTO.getMediaId(), postDTO.getDescription(),
                postDTO.getTags().stream().map(TagMapper::toEntity).collect(Collectors.toSet()));
    }

    public static PostResponseDTO toDto (Post post) {
        return new PostResponseDTO(post.getId());
    }
}
