package rs.ac.uns.ftn.devops.tim5.nistagrampost.service.impl;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.exception.ResourceNotFoundException;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.kafka.Constants;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.Post;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.Reaction;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.User;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.repository.PostRepository;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.repository.ReactionRepository;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.service.MailService;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.service.PostService;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.service.TagService;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.service.UserService;

import javax.mail.MessagingException;
import java.util.Collection;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final TagService tagService;

    public PostServiceImpl(PostRepository postRepository,
                           UserService userService,
                           TagService tagService) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.tagService = tagService;
    }

    @Override
    public Post create(Post post, String username) throws ResourceNotFoundException {
        post.setUser(userService.findByUsername(username));
        post.setTags(tagService.getTagsForPost(post.getTags()));
        return postRepository.save(post);
    }

    @Override
    public void delete(Post post) {
        postRepository.delete(post);
    }

    @Override
    public Post findById(Long id) throws ResourceNotFoundException {
        return postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post"));
    }


}

