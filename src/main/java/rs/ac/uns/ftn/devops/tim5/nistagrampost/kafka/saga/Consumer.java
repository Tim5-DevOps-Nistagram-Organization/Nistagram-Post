package rs.ac.uns.ftn.devops.tim5.nistagrampost.kafka.saga;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.exception.ResourceNotFoundException;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.kafka.Constants;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.kafka.Message;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.kafka.PostMessage;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.kafka.UserMessage;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.service.PostService;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.service.UserService;

@Service
public class Consumer {

    private final PostService postService;
    private final UserService userService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Gson gson;

    @Autowired
    public Consumer(PostService postService, UserService userService, KafkaTemplate<String, String> kafkaTemplate,
                    Gson gson) {
        this.postService = postService;
        this.userService = userService;
        this.kafkaTemplate = kafkaTemplate;
        this.gson = gson;
    }

    @KafkaListener(topics = Constants.POST_TOPIC, groupId = Constants.GROUP)
    public void getMessage(String msg) throws ResourceNotFoundException {
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
            postService.delete(postMessage.getPostId());
            //todo add mail
        }
    }
}
