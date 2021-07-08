package rs.ac.uns.ftn.devops.tim5.nistagrampost.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.exception.ResourceNotFoundException;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.kafka.Constants;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.kafka.saga.PostOrchestrator;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.kafka.saga.ReactionOrchestrator;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.Comment;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.Post;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.Reaction;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.UnappropriatedContent;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.enums.UnappropriatedContentState;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.repository.UnappropriatedContentRepository;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.service.*;

import javax.mail.MessagingException;
import java.util.Collection;

@Service
public class UnappropriatedContentServiceImpl implements UnappropriatedContentService {

    private final UnappropriatedContentRepository unappropriatedContentRepository;
    private final UserService userService;
    private final PostService postService;
    private final MailService mailService;
    private final ReactionService reactionService;
    private final ReactionOrchestrator reactionOrchestrator;
    private final PostOrchestrator postOrchestrator;
    private final CommentService commentService;

    @Autowired
    public UnappropriatedContentServiceImpl(
            UnappropriatedContentRepository unappropriatedContentRepository,
            PostService postService,
            UserService userService,
            MailService mailService,
            ReactionService reactionService,
            ReactionOrchestrator reactionOrchestrator,
            PostOrchestrator postOrchestrator,
            CommentService commentService) {
        this.unappropriatedContentRepository = unappropriatedContentRepository;
        this.userService = userService;
        this.postService = postService;
        this.mailService = mailService;
        this.reactionService = reactionService;
        this.reactionOrchestrator = reactionOrchestrator;
        this.postOrchestrator = postOrchestrator;
        this.commentService = commentService;
    }

    @Override
    public UnappropriatedContent findById(Long id) throws ResourceNotFoundException {
        return unappropriatedContentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Unappropriated Content"));
    }

    @Override
    public UnappropriatedContent create(UnappropriatedContent content, String username) throws ResourceNotFoundException {
        postService.findById(content.getPostId());
        content.setInitiator(userService.findByUsername(username));
        content.setState(UnappropriatedContentState.REQUESTED);
        return unappropriatedContentRepository.save(content);
    }

    @Override
    public UnappropriatedContent approve(Long id) throws ResourceNotFoundException, MessagingException {
        UnappropriatedContent old = this.findById(id);
        Post post = postService.findById(old.getPostId());

        // delete all Reactions for post
        Collection<Reaction> postReactions = reactionService.findAllByPostId(old.getPostId());
        for (Reaction reaction : postReactions) {
            reactionService.delete(reaction.getId());
            reactionOrchestrator.startSaga(reaction, Constants.DELETE_ACTION);
        }

        // delete all Post Comments
        Collection<Comment> postComments = commentService.findAllByPostId(old.getPostId());
        for (Comment comment : postComments) {
            commentService.delete(comment.getId());
        }

        //delete Post and start delete saga for Post
        postOrchestrator.startSaga(post, Constants.DELETE_ACTION);
        postService.delete(post);

        old.setState(UnappropriatedContentState.CONFIRMED);

        String email = old.getInitiator().getEmail();
        String subject = "Post deleted due Unappropriate contentn reports!";
        String emailMessage = "<html><head><meta charset=\"UTF-8\"></head>"
                + "<body><h3>Nistagram app - Post deleted due Unappropriate contentn reports!</h3><br>"
                + "</p></div></body></html>";
        mailService.sendMail(email, subject, emailMessage);
        return unappropriatedContentRepository.save(old);
    }

    @Override
    public UnappropriatedContent reject(Long id) throws ResourceNotFoundException {
        UnappropriatedContent old = this.findById(id);
        old.setState(UnappropriatedContentState.REJECTED);
        return unappropriatedContentRepository.save(old);
    }

    @Override
    public Collection<UnappropriatedContent> findAllRequested() throws ResourceNotFoundException {
        Collection<UnappropriatedContent> retVal =
                unappropriatedContentRepository.findAllByState(UnappropriatedContentState.REQUESTED);
        for (UnappropriatedContent val : retVal) {
            val.setPost(postService.findById(val.getPostId()));
        }
        return retVal;
    }

    @Override
    public void delete(Long id) throws ResourceNotFoundException {
        UnappropriatedContent unappropriatedContent = this.findById(id);
        unappropriatedContentRepository.delete(unappropriatedContent);
    }
}
