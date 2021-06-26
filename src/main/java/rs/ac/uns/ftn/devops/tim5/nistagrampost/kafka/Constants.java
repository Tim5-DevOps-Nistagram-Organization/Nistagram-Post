package rs.ac.uns.ftn.devops.tim5.nistagrampost.kafka;

public class Constants {

    public static final String GROUP = "my_group_id";

    public static final String AUTH_TOPIC = "auth_topic";
    public static final String AUTH_POST_TOPIC = "auth_post_topic";

    public static final String USER_ORCHESTRATOR_TOPIC = "user_orchestrator_topic";
    public static final String POST_TOPIC = "post_topic";

    public static final String START_ACTION = "start_action";
    public static final String DONE_ACTION = "done_action";
    public static final String ERROR_ACTION = "error_action";
    public static final String ROLLBACK_ACTION = "rollback_action";
    public static final String ROLLBACK_DONE_ACTION = "rollback_done_action";
}
