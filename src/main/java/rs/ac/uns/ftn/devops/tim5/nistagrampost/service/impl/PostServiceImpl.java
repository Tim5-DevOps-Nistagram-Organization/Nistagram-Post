package rs.ac.uns.ftn.devops.tim5.nistagrampost.service.impl;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.exception.ResourceNotFoundException;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.Post;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.User;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.repository.PostRepository;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.service.MailService;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.service.PostService;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.service.TagService;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.service.UserService;

import javax.mail.MessagingException;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final TagService tagService;
    private final MailService mailService;

    public PostServiceImpl(PostRepository postRepository, UserService userService, TagService tagService,
                           MailService mailService) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.tagService = tagService;
        this.mailService = mailService;
    }

    @Override
    public Post create(Post post, String username) throws ResourceNotFoundException {
        post.setUser(userService.findByUsername(username));
        post.setTags(tagService.getTagsForPost(post.getTags()));
        return postRepository.save(post);
    }

    @Override
    public void delete(Long id) throws ResourceNotFoundException, MessagingException {
        Post post = this.findById(id);
        String email = post.getUser().getEmail();
        postRepository.delete(post);
        String subject = "Something went wrong!";
        String message = "<html><head><meta charset=\"UTF-8\"></head>"
                + "<body><h3>Nistagram app - Something went wrong!</h3><br>"
                + "<div><p>Something went wrong with publishing post, please try again!"
                + "</p></div></body></html>";
        mailService.sendMail(email, subject, message);
    }

    @Override
    public Post findById(Long id) throws ResourceNotFoundException {
        return postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post"));
    }


}

