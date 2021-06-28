package rs.ac.uns.ftn.devops.tim5.nistagrampost.kafka.saga;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.kafka.Constants;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.Reaction;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.kafka.PostMessage;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.kafka.ReactionMessage;


@Service
public class ReactionOrchestrator {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Gson gson;

    @Autowired
    public ReactionOrchestrator(KafkaTemplate<String, String> kafkaTemplate, Gson gson) {
        this.kafkaTemplate = kafkaTemplate;
        this.gson = gson;
    }


    /*
    Method which start Saga for add new Reaction or update Reaction or delete Reaction.
    Send ReactionMessage to Search service with all parameters
    Action parameter is one of following:
        Constants.START_ACTION
        Constants.UPDATE_ACTION
        Constants.DELETE_ACTION
    * */

    @Async
    public void startSaga(Reaction reaction, String action) {
        ReactionMessage message =
                new ReactionMessage(Constants.SEARCH_TOPIC,
                        Constants.REACTION_ORCHESTRATOR_TOPIC, action,
                        reaction.getId(), reaction.getReaction().getValue(),
                        reaction.getPost().getId(), reaction.getUser().getUsername());
        this.kafkaTemplate.send(message.getTopic(), gson.toJson(message));
    }

    @KafkaListener(topics = Constants.REACTION_ORCHESTRATOR_TOPIC, groupId = Constants.GROUP)
    public void getMessageOrchestrator(String msg) {
        PostMessage message = gson.fromJson(msg, PostMessage.class);
        if (message.getAction().equals(Constants.ERROR_ACTION)) {
            message.setDetails(Constants.REACTION_TOPIC, Constants.REACTION_ORCHESTRATOR_TOPIC, Constants.ROLLBACK_ACTION);
            kafkaTemplate.send(message.getTopic(), gson.toJson(message));
        }
    }
}
