package rs.ac.uns.ftn.devops.tim5.nistagrampost.saga;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.dto.PostSearchDTO;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostMessage {
    private String topic;
    private String replayTopic;
    private String action;
    private PostSearchDTO data;

    public void setDetails(String topic, String replayTopic, String action) {
        this.topic = topic;
        this.replayTopic = replayTopic;
        this.action = action;
    }
}
