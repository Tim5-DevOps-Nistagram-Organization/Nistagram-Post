package rs.ac.uns.ftn.devops.tim5.nistagrampost.service;

import rs.ac.uns.ftn.devops.tim5.nistagrampost.exception.ResourceNotFoundException;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.Comment;

import java.util.Collection;

public interface CommentService {

    Comment findById(Long id) throws ResourceNotFoundException;
    Comment save(Comment comment, String writerUsername) throws ResourceNotFoundException;
    void delete(Long id) throws ResourceNotFoundException;
    Collection<Comment> findAllByPostId(Long postId);
}
