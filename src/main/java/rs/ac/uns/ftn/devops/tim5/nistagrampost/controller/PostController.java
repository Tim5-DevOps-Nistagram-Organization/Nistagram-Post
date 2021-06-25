package rs.ac.uns.ftn.devops.tim5.nistagrampost.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.dto.PostRequestDTO;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.mapper.PostMapper;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.Post;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.saga.PostOrchestrator;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.service.PostService;

@RestController
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private final PostOrchestrator postOrchestrator;

    @Autowired
    public PostController(PostService postService, PostOrchestrator postOrchestrator) {
        this.postService = postService;
        this.postOrchestrator = postOrchestrator;
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody PostRequestDTO postRequestDTO) throws Exception {
        Post post = postService.create(PostMapper.toEntity(postRequestDTO));
        postOrchestrator.startSaga(post);
        return new ResponseEntity<>("Sačuvan post sa id " + post.getId(), HttpStatus.OK);
    }


}
