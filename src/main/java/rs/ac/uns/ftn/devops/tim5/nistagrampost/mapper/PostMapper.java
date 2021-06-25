package rs.ac.uns.ftn.devops.tim5.nistagrampost.mapper;

import rs.ac.uns.ftn.devops.tim5.nistagrampost.dto.PostRequestDTO;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.dto.PostSearchDTO;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.Post;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.User;

import java.util.stream.Collectors;

public class PostMapper {

    public static Post toEntity(PostRequestDTO postDTO) {
        return new Post(postDTO.getMediaId(), postDTO.getDescription(), new User(postDTO.getUserUsername()),
                postDTO.getTags().stream().map(TagMapper::toEntity).collect(Collectors.toSet()));
    }

    public static PostSearchDTO toSearchDTO(Post post) {
        return new PostSearchDTO(post.getId(), post.getMediaId(),
                post.getTags().stream().map(TagMapper::toDTO).collect(Collectors.toSet()));
    }
}
