package rs.ac.uns.ftn.devops.tim5.nistagrampost.integration;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.constants.*;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.dto.*;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.exception.ResourceNotFoundException;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.mapper.CommentMapper;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.mapper.PostMapper;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.mapper.ReactionMapper;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.mapper.UnappropriatedContentMapper;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.*;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.enums.ReactionEnum;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.enums.UnapropriatedContentState;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.repository.PostRepository;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.service.*;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class PostIntegrationTest {

    @Autowired
    private PostService postService;

    @Autowired
    PostRepository postRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ReactionService reactionService;

    @Autowired
    private UnappropriatedContentService unappropriatedContentService;

    private Set<TagDTO> tagDTOSet;


    @Before
    public void setUp() {
        userService.create(UserConstants.VALID_USERNAME, UserConstants.VALID_EMAIL, "");

        // create tagDTOSet
        tagDTOSet = new HashSet<>();
        tagDTOSet.addAll(
            TagConstants.VALID_TAG_NAMES.stream().map(TagDTO::new).collect(Collectors.toList())
        );
    }

    /*
     *  Method test PostService create
     *  check if post is added successfully with tags
     *
     *  Verification is done on service level because saga start in controller
     *  and for saga is needed all other service which participate in saga,
     *  Also auth service is needed on Controller level
     *
     *
     * */
    @Test
    public void testPostCreation_Success() throws ResourceNotFoundException {

        PostRequestDTO postRequestDTO =
                new PostRequestDTO(MediaConstants.VALID_ID_1, PostConstants.VALID_POST_MESSAGE, tagDTOSet);

        Post post = postService.create(PostMapper.toEntity(postRequestDTO), UserConstants.VALID_USERNAME);

        assertNotNull(post.getId());
        assertEquals(PostConstants.VALID_POST_MESSAGE, post.getDescription());
        assertEquals(MediaConstants.VALID_ID_1, post.getMediaId());
        assertEquals(UserConstants.VALID_USERNAME, post.getUser().getUsername());
        assertEquals(TagConstants.NUMBER_OF_TAGS, post.getTags().size());


    }

    /*
     *   Method test Comment creation on valid Post
     *   created in testPostCreation_Success
     *
     * */
    @Test
    public void testCommentCreation_Success() throws ResourceNotFoundException {

        PostRequestDTO postRequestDTO =
                new PostRequestDTO(MediaConstants.VALID_ID_1, PostConstants.VALID_POST_MESSAGE, tagDTOSet);

        Post post = postService.create(PostMapper.toEntity(postRequestDTO), UserConstants.VALID_USERNAME);


        Long VALID_POST_ID = post.getId();

        CommentCreateDTO commentCreateDTO =
                new CommentCreateDTO(CommentConstants.COMMENT_MESSAGE, VALID_POST_ID);
        Comment comment = commentService.save(CommentMapper.toEntity(commentCreateDTO), UserConstants.VALID_USERNAME);

        assertNotNull(comment.getId());
        assertEquals(CommentConstants.COMMENT_MESSAGE , comment.getMessage());
        assertEquals(VALID_POST_ID, comment.getPost().getId());
        assertEquals(UserConstants.VALID_USERNAME,comment.getWriter().getUsername());
    }



    /*
     *   Method test Comment creation on invalid Post
     *   Post id is set to POST_ID + 1, which certainly does not exist
     *   should raise ResourceNotFoundException
     *
     * */
    @Test(expected = ResourceNotFoundException.class)
    public void testCommentCreation_InvalidPostId() throws ResourceNotFoundException {

        CommentCreateDTO commentCreateDTO =
                new CommentCreateDTO(CommentConstants.COMMENT_MESSAGE, PostConstants.INVALID_POST_ID);
        commentService.save(CommentMapper.toEntity(commentCreateDTO), UserConstants.VALID_USERNAME);
    }


    /*
     *   Method test Reaction creation on valid Post
     *   created in testPostCreation_Success
     *
     * */
    @Test
    public void testReactionCreation_Success() throws ResourceNotFoundException {

        PostRequestDTO postRequestDTO =
                new PostRequestDTO(MediaConstants.VALID_ID_1, PostConstants.VALID_POST_MESSAGE, tagDTOSet);

        Post post = postService.create(PostMapper.toEntity(postRequestDTO), UserConstants.VALID_USERNAME);

        Long VALID_POST_ID = post.getId();

        ReactionCreateRequestDTO requestDTO =
                new ReactionCreateRequestDTO(VALID_POST_ID, ReactionEnum.LIKE.getValue());
        Reaction reaction = reactionService.create(ReactionMapper.newToEntity(requestDTO), UserConstants.VALID_USERNAME);

        assertNotNull(reaction.getId());
        assertEquals( ReactionEnum.LIKE.getValue(), reaction.getReaction().getValue());
        assertEquals(VALID_POST_ID, reaction.getPost().getId());
        assertEquals(UserConstants.VALID_USERNAME,reaction.getUser().getUsername());
    }

    /*
     *   Method test Reaction creation on invalid Post
     *   Post id is set to POST_ID + 1, which certainly does not exist
     *   should raise ResourceNotFoundException
     *
     * */
    @Test(expected = ResourceNotFoundException.class)
    public void testReactionCreation_InvalidPostId() throws ResourceNotFoundException {

        ReactionCreateRequestDTO requestDTO =
                new ReactionCreateRequestDTO( PostConstants.INVALID_POST_ID, ReactionEnum.LIKE.getValue());
        reactionService.create(ReactionMapper.newToEntity(requestDTO), UserConstants.VALID_USERNAME);

    }

    /*
     *   Method test UnappropriatedContent creation on valid Post
     *   created in testPostCreation_Success
     *
     * */
    @Test
    public void testUnappropriatedContentRequestCreation_Success() throws ResourceNotFoundException {

        PostRequestDTO postRequestDTO =
                new PostRequestDTO(MediaConstants.VALID_ID_1, PostConstants.VALID_POST_MESSAGE, tagDTOSet);

        Post post = postService.create(PostMapper.toEntity(postRequestDTO), UserConstants.VALID_USERNAME);

        Long VALID_POST_ID = post.getId();

        UnappropriatedContentCreateRequestDTO requestDTO =
                new UnappropriatedContentCreateRequestDTO(VALID_POST_ID, UnappropriatedConstants.DESCRIPTION);
        UnappropriatedContent unappropriatedContent = unappropriatedContentService.create(
                        UnappropriatedContentMapper.newToEntity(requestDTO), UserConstants.VALID_USERNAME);

        assertNotNull(unappropriatedContent.getId());
        assertEquals(  UnappropriatedConstants.DESCRIPTION, unappropriatedContent.getDescription());
        assertEquals(UserConstants.VALID_USERNAME,unappropriatedContent.getInitiator().getUsername());
        assertEquals(VALID_POST_ID, unappropriatedContent.getPostId());
        assertEquals(UnapropriatedContentState.REQUESTED.getValue(), unappropriatedContent.getState().getValue());

    }

    /*
     *   Method test UnappropriatedContent creation on invalid Post
     *   Post id is set to POST_ID + 1, which certainly does not exist
     *   should raise ResourceNotFoundException
     *
     * */
    @Test(expected = ResourceNotFoundException.class)
    public void testUnappropriatedContentRequestCreation_InvalidPostId() throws ResourceNotFoundException {

        UnappropriatedContentCreateRequestDTO requestDTO =
                new UnappropriatedContentCreateRequestDTO(
                        PostConstants.INVALID_POST_ID,
                        UnappropriatedConstants.DESCRIPTION);
        unappropriatedContentService.create(
                UnappropriatedContentMapper.newToEntity(requestDTO), UserConstants.VALID_USERNAME);

    }
}




