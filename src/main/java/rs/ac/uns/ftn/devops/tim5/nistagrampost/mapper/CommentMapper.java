package rs.ac.uns.ftn.devops.tim5.nistagrampost.mapper;

import rs.ac.uns.ftn.devops.tim5.nistagrampost.dto.CommentCreateDTO;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.dto.CommentDTO;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.Comment;

public class CommentMapper {

    private CommentMapper() {
    }

    public static Comment toEntity(CommentCreateDTO createDTO) {
        return new Comment(createDTO.getPostId(), createDTO.getMessage());
    }

    public static CommentDTO toDTO(Comment comment) {
        return new CommentDTO(comment.getWriter().getUsername(), comment.getMessage(), comment.getDate());
    }
}
