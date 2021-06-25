package rs.ac.uns.ftn.devops.tim5.nistagrampost.saga;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.service.PostService;

@Service
public class Consumer {

    private final PostService postService;
    private final Gson gson;

    @Autowired
    public Consumer(PostService postService, Gson gson) {
        this.postService = postService;
        this.gson = gson;
    }

    @KafkaListener(topics = Constants.POST_TOPIC, groupId = "my_group_id")
    public void getMessage(String msg) {
        PostMessage message = gson.fromJson(msg, PostMessage.class);
        postService.delete(message.getData().getPostId());
        //todo mejl
    }
}
