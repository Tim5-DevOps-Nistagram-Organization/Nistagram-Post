package rs.ac.uns.ftn.devops.tim5.nistagrampost.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.dto.CommentCreateDTO;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.dto.CommentDTO;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.dto.PostRequestDTO;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.dto.UnappropriatedContentResponseDTO;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.exception.ResourceNotFoundException;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.kafka.Constants;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.mapper.CommentMapper;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.mapper.PostMapper;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.mapper.UnappropriatedContentMapper;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.Comment;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.Post;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.Reaction;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.service.CommentService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/comment")
public class CommentController {

    private CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_REGULAR') || hasRole('ROLE_AGENT')")
    public ResponseEntity<CommentDTO> create(@Valid @RequestBody CommentCreateDTO commentCreateDTO,
                                             Principal principal) throws ResourceNotFoundException {
        Comment comment = commentService.save(CommentMapper.toEntity(commentCreateDTO), principal.getName());
        return new ResponseEntity<>(CommentMapper.toDTO(comment), HttpStatus.OK);
    }

    @GetMapping(path="/getByPost/{postId}")
    @PreAuthorize("hasRole('ROLE_REGULAR') || hasRole('ROLE_AGENT') || hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<CommentDTO>> getAllBYPostId(@Valid @PathVariable Long postId)
            throws ResourceNotFoundException {
        List<CommentDTO> retVal = commentService.findAllByPostId(postId).stream()
                        .map(comment -> CommentMapper.toDTO(comment))
                        .collect(Collectors.toList());
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{commentId}")
    @PreAuthorize("hasRole('ROLE_REGULAR') || hasRole('ROLE_AGENT') || hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> delete(@Valid @PathVariable Long commentId) throws ResourceNotFoundException {
        commentService.delete(commentId);
        return new ResponseEntity<>("Comment is successfully removed.", HttpStatus.OK);
    }




}
