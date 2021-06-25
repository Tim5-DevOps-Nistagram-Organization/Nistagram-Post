package rs.ac.uns.ftn.devops.tim5.nistagrampost.service.impl;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.exception.ResourceNotFoundException;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.Post;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.repository.PostRepository;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.service.PostService;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.service.TagService;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.service.UserService;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final TagService tagService;

    public PostServiceImpl(PostRepository postRepository, UserService userService, TagService tagService) {
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
    public void delete(Long id) {
        postRepository.deleteById(id);
    }
}
