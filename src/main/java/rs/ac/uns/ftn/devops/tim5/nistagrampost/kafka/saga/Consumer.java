package rs.ac.uns.ftn.devops.tim5.nistagrampost.kafka.saga;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.exception.ResourceNotFoundException;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.kafka.Constants;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.Post;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.kafka.*;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.service.*;

import javax.mail.MessagingException;

@Service
public class Consumer {

    private final PostService postService;
    private final UserService userService;
    private final ReactionService reactionService;
    private final UnappropriatedContentService unappropriatedContentService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Gson gson;
    private final MailService mailService;

    @Autowired
    public Consumer(PostService postService,
                    UserService userService,
                    ReactionService reactionService,
                    UnappropriatedContentService unappropriatedContentService,
                    KafkaTemplate<String, String> kafkaTemplate,
                    Gson gson,
                    MailService mailService) {
        this.postService = postService;
        this.userService = userService;
        this.reactionService = reactionService;
        this.unappropriatedContentService = unappropriatedContentService;
        this.kafkaTemplate = kafkaTemplate;
        this.gson = gson;
        this.mailService = mailService;
    }

    @KafkaListener(topics = Constants.POST_TOPIC, groupId = Constants.GROUP)
    public void getMessage(String msg) throws ResourceNotFoundException, MessagingException {
        Message message = gson.fromJson(msg, Message.class);
        if (message.getReplayTopic().equals(Constants.USER_ORCHESTRATOR_TOPIC)) {
            UserMessage userMessage = gson.fromJson(msg, UserMessage.class);
            if (userMessage.getAction().equals(Constants.START_ACTION)) {
                try {
                    userService.create(userMessage.getUsername(), userMessage.getEmail());
                    userMessage.setDetails(userMessage.getReplayTopic(), Constants.POST_TOPIC, Constants.DONE_ACTION);
                } catch (Exception e) {
                    userMessage.setDetails(userMessage.getReplayTopic(), Constants.POST_TOPIC, Constants.ERROR_ACTION);
                }
            } else if (userMessage.getAction().equals(Constants.ROLLBACK_ACTION)) {
                userService.delete(userMessage.getUsername());
                userMessage.setDetails(userMessage.getReplayTopic(), Constants.POST_TOPIC, Constants.ROLLBACK_DONE_ACTION);
            }
            kafkaTemplate.send(userMessage.getTopic(), gson.toJson(userMessage));
        } else if (message.getReplayTopic().equals(Constants.POST_ORCHESTRATOR_TOPIC) &&
                message.getAction().equals(Constants.ROLLBACK_ACTION)) {
            PostMessage postMessage = gson.fromJson(msg, PostMessage.class);
            Post post = postService.findById(postMessage.getPostId());
            String email = post.getUser().getEmail();
            postService.delete(post);
            String subject = "Something went wrong!";
            String emailMessage = "<html><head><meta charset=\"UTF-8\"></head>"
                    + "<body><h3>Nistagram app - Something went wrong!</h3><br>"
                    + "<div><p>Something went wrong with publishing post, please try again!"
                    + "</p></div></body></html>";
            mailService.sendMail(email, subject, emailMessage);
        }  else if (message.getReplayTopic().equals(Constants.REACTION_ORCHESTRATOR_TOPIC) &&
            message.getAction().equals(Constants.ROLLBACK_ACTION)) {
        ReactionMessage reactionMessage = gson.fromJson(msg, ReactionMessage.class);
        reactionService.delete(reactionMessage.getReactionId());
        //@TODO: da li slati adminima mejl da je puklo negde
        } else if (message.getReplayTopic().equals(Constants.UNAPPROPRIATED_CONTENT_ORCHESTRATOR_TOPIC) &&
                message.getAction().equals(Constants.ROLLBACK_ACTION)) {
            ContentReportMessage contentReportMessage = gson.fromJson(msg, ContentReportMessage.class);
            unappropriatedContentService.delete(contentReportMessage.getUnappropriatedContentId());
        }
    }
}
