package rs.ac.uns.ftn.devops.tim5.nistagrampost.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.dto.ReactionCreateRequestDTO;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.dto.ReactionUpdateRequestDTO;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.kafka.Constants;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.kafka.saga.ReactionOrchestrator;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.mapper.ReactionMapper;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.Reaction;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.service.ReactionService;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/reaction")
public class ReactionController {

    private final ReactionService reactionService;
    private final ReactionOrchestrator reactionOrchestrator;

    @Autowired
    public ReactionController(ReactionService reactionService, ReactionOrchestrator reactionOrchestrator) {
        this.reactionService = reactionService;
        this.reactionOrchestrator = reactionOrchestrator;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_REGULAR') || hasRole('ROLE_AGENT')")
    public ResponseEntity<String> create(@Valid @RequestBody ReactionCreateRequestDTO reactionRequestDTO, Principal principal) throws Exception {
        Reaction reaction = reactionService.create(ReactionMapper.newToEntity(reactionRequestDTO), principal.getName());
        reactionOrchestrator.startSaga(reaction, Constants.START_ACTION);
        return new ResponseEntity<>("Reaction is successfully saved.", HttpStatus.OK);
    }

    @PutMapping
    @PreAuthorize("hasRole('ROLE_REGULAR') || hasRole('ROLE_AGENT')")
    public ResponseEntity<String> update(@Valid @RequestBody ReactionUpdateRequestDTO reactionRequestDTO, Principal principal) throws Exception {
        Reaction reaction = reactionService.update(ReactionMapper.updateToEntity(reactionRequestDTO));
        reactionOrchestrator.startSaga(reaction, Constants.UPDATE_ACTION);
        return new ResponseEntity<>("Reaction is successfully updated.", HttpStatus.OK);
    }

    @DeleteMapping(path = "/{reactionId}")
    @PreAuthorize("hasRole('ROLE_REGULAR') || hasRole('ROLE_AGENT')")
    public ResponseEntity<String> delete(@Valid @PathVariable Long reactionId) throws Exception {
        reactionService.delete(reactionId);
        Reaction reaction = new Reaction(reactionId);
        reactionOrchestrator.startSaga(reaction, Constants.DELETE_ACTION);
        return new ResponseEntity<>("Reaction is successfully removed.", HttpStatus.OK);
    }

}
