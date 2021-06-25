package rs.ac.uns.ftn.devops.tim5.nistagrampost.saga;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.mapper.PostMapper;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.Post;

@Service
public class PostOrchestrator {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Gson gson;

    @Autowired
    public PostOrchestrator(KafkaTemplate<String, String> kafkaTemplate, Gson gson) {
        this.kafkaTemplate = kafkaTemplate;
        this.gson = gson;
    }

    @Async
    public void startSaga(Post post) {
        PostMessage message = new PostMessage(Constants.SEARCH_TOPIC, Constants.POST_ORCHESTRATOR_TOPIC, Constants.START_ACTION,
                PostMapper.toSearchDTO(post));
        this.kafkaTemplate.send(message.getTopic(), gson.toJson(message));
    }

    @KafkaListener(topics = Constants.POST_ORCHESTRATOR_TOPIC, groupId = "my_group_id")
    public void getMessageOrchestrator(String msg) {
        PostMessage message = gson.fromJson(msg, PostMessage.class);
        if (message.getAction().equals(Constants.ERROR_ACTION)) {
            message.setDetails(Constants.POST_TOPIC, Constants.POST_ORCHESTRATOR_TOPIC, Constants.ROLLBACK_ACTION);
            kafkaTemplate.send(message.getTopic(), gson.toJson(message));
        }
    }

}
