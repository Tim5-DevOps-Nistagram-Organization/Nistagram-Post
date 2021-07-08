package rs.ac.uns.ftn.devops.tim5.nistagrampost.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.exception.ResourceNotFoundException;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.Comment;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.Post;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.User;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.repository.CommentRepository;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.service.CommentService;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.service.PostService;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.service.UserService;

import java.util.Collection;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final UserService userService;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository,
                              PostService postService,
                              UserService userService) {
        this.commentRepository = commentRepository;
        this.postService = postService;
        this.userService = userService;
    }

    @Override
    public Comment findById(Long id) throws ResourceNotFoundException {
        return commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment"));
    }

    @Override
    public Comment save(Comment comment, String writerUsername) throws ResourceNotFoundException {
        Post post = postService.findById(comment.getPostId());
        User writer = userService.findByUsername(writerUsername);
        comment.setWriter(writer);
        comment.setPost(post);
        return commentRepository.save(comment);
    }

    @Override
    public void delete(Long id) throws ResourceNotFoundException {
        Comment comment = findById(id);
        commentRepository.delete(comment);
    }

    @Override
    public Collection<Comment> findAllByPostId(Long postId) {
        return commentRepository.findAllByPostId(postId);
    }
}
