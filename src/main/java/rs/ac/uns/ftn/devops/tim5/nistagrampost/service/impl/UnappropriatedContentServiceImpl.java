package rs.ac.uns.ftn.devops.tim5.nistagrampost.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.exception.ResourceNotFoundException;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.kafka.Constants;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.kafka.saga.PostOrchestrator;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.kafka.saga.ReactionOrchestrator;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.Post;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.Reaction;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.UnappropriatedContent;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.enums.UnapropriatedContentState;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.repository.UnappropriatedContentRepository;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.service.*;

import javax.mail.MessagingException;
import java.util.Collection;

@Service
public class UnappropriatedContentServiceImpl implements UnappropriatedContentService {

    private UnappropriatedContentRepository unappropriatedContentRepository;
    private UserService userService;
    private PostService postService;
    private MailService mailService;
    private ReactionService reactionService;
    private ReactionOrchestrator reactionOrchestrator;
    private PostOrchestrator postOrchestrator;

    @Autowired
    public UnappropriatedContentServiceImpl(
            UnappropriatedContentRepository unappropriatedContentRepository,
            PostService postService,
            UserService userService,
            MailService mailService,
            ReactionService reactionService,
            ReactionOrchestrator reactionOrchestrator,
            PostOrchestrator postOrchestrator) {
        this.unappropriatedContentRepository = unappropriatedContentRepository;
        this.userService = userService;
        this.postService =postService;
        this.mailService = mailService;
        this.reactionService = reactionService;
        this.reactionOrchestrator = reactionOrchestrator;
        this.postOrchestrator = postOrchestrator;
    }

    @Override
    public UnappropriatedContent findById(Long id) throws ResourceNotFoundException {
        return  unappropriatedContentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Unappropriated Content"));
    }

    @Override
    public UnappropriatedContent create(UnappropriatedContent content, String username) throws ResourceNotFoundException {
        content.setInitiator(userService.findByUsername(username));
        content.setState(UnapropriatedContentState.REQUESTED);
        return unappropriatedContentRepository.save(content);
    }

    @Override
    public UnappropriatedContent approve(Long id) throws ResourceNotFoundException, MessagingException {
        UnappropriatedContent old = this.findById(id);
        Post post = postService.findById(old.getPostId());

        // delete all Reactions for post
        Collection<Reaction> postReactions = reactionService.findAllByPostId(old.getPostId());
        for (Reaction reaction: postReactions) {
            reactionService.delete(reaction.getId());
            reactionOrchestrator.startSaga(reaction, Constants.DELETE_ACTION);
        }
        //delete Post and start delete saga for Post
        postOrchestrator.startSaga(post, Constants.DELETE_ACTION);
        postService.delete(post);

        old.setState(UnapropriatedContentState.CONFIRMED);

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
        old.setState(UnapropriatedContentState.REJECTED);
        return unappropriatedContentRepository.save(old);
    }

    @Override
    public Collection<UnappropriatedContent> findAllRequested() throws ResourceNotFoundException {
        Collection<UnappropriatedContent> retVal =
                unappropriatedContentRepository.findAllByState(UnapropriatedContentState.REQUESTED);
        for (UnappropriatedContent val: retVal) {
            val.setPost(postService.findById(val.getPostId()));
        }
        return  retVal;
    }

    @Override
    public void delete(Long id) throws ResourceNotFoundException {
        UnappropriatedContent unappropriatedContent = this.findById(id);
        unappropriatedContentRepository.delete(unappropriatedContent);
    }
}
