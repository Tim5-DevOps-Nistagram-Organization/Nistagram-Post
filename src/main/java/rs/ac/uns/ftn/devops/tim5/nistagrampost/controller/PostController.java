package rs.ac.uns.ftn.devops.tim5.nistagrampost.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.dto.PostRequestDTO;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.exception.ResourceNotFoundException;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.kafka.Constants;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.kafka.saga.PostOrchestrator;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.mapper.PostMapper;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.Post;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.service.PostService;

import javax.validation.Valid;
import java.security.Principal;

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
    @PreAuthorize("hasRole('ROLE_REGULAR') || hasRole('ROLE_AGENT')")
    public ResponseEntity<String> create(@Valid @RequestBody PostRequestDTO postRequestDTO,
                                         Principal principal) throws ResourceNotFoundException {
        Post post = postService.create(PostMapper.toEntity(postRequestDTO), principal.getName());
        postOrchestrator.startSaga(post, Constants.START_ACTION);
        return new ResponseEntity<>("Post is successfully saved.", HttpStatus.OK);
    }

}
