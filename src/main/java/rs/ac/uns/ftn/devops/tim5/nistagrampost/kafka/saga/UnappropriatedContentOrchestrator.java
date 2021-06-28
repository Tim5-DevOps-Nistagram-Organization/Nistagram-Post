package rs.ac.uns.ftn.devops.tim5.nistagrampost.kafka.saga;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.kafka.Constants;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.UnappropriatedContent;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.kafka.ContentReportMessage;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.kafka.PostMessage;

@Service
public class UnappropriatedContentOrchestrator {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Gson gson;

    @Autowired
    public UnappropriatedContentOrchestrator(KafkaTemplate<String, String> kafkaTemplate, Gson gson) {
        this.kafkaTemplate = kafkaTemplate;
        this.gson = gson;
    }


    /*
    Method which start Saga for add new UnappropriatedContent or
        update UnappropriatedContent or delete UnappropriatedContent.
    Send ReactionMessage to Search service with all parameters
    Action parameter is one of following:
        Constants.START_ACTION
        Constants.UPDATE_ACTION
        Constants.DELETE_ACTION
    * */

    @Async
    public void startSaga(UnappropriatedContent content, String action) {
        ContentReportMessage message =
                new ContentReportMessage(Constants.SEARCH_TOPIC,
                        Constants.UNAPPROPRIATED_CONTENT_ORCHESTRATOR_TOPIC, action,
                        content.getId(), content.getDescription(),
                        content.getPost().getId(), content.getInitiator().getUsername());
        this.kafkaTemplate.send(message.getTopic(), gson.toJson(message));
    }

    @KafkaListener(topics = Constants.UNAPPROPRIATED_CONTENT_ORCHESTRATOR_TOPIC, groupId = Constants.GROUP)
    public void getMessageOrchestrator(String msg) {
        PostMessage message = gson.fromJson(msg, PostMessage.class);
        if (message.getAction().equals(Constants.ERROR_ACTION)) {
            message.setDetails(Constants.REACTION_TOPIC,
                    Constants.UNAPPROPRIATED_CONTENT_ORCHESTRATOR_TOPIC,
                    Constants.ROLLBACK_ACTION);
            kafkaTemplate.send(message.getTopic(), gson.toJson(message));
        }
    }
}
