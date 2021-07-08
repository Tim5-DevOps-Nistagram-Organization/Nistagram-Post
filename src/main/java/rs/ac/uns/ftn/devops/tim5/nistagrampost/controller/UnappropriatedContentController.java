package rs.ac.uns.ftn.devops.tim5.nistagrampost.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.dto.UnappropriatedContentCreateRequestDTO;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.dto.UnappropriatedContentResponseDTO;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.exception.ResourceNotFoundException;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.kafka.Constants;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.kafka.saga.UnappropriatedContentOrchestrator;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.mapper.UnappropriatedContentMapper;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.UnappropriatedContent;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.service.UnappropriatedContentService;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/unappropriatedContent")
public class UnappropriatedContentController {

    private final UnappropriatedContentService unappropriatedContentService;
    private final UnappropriatedContentOrchestrator unappropriatedContentOrchestrator;

    @Autowired
    public UnappropriatedContentController(
            UnappropriatedContentService unappropriatedContentService,
            UnappropriatedContentOrchestrator unappropriatedContentOrchestrator
    ) {
        this.unappropriatedContentService = unappropriatedContentService;
        this.unappropriatedContentOrchestrator = unappropriatedContentOrchestrator;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_REGULAR') || hasRole('ROLE_AGENT')")
    public ResponseEntity<String> create(
            @Valid @RequestBody UnappropriatedContentCreateRequestDTO contentRequestDTO,
            Principal principal) throws ResourceNotFoundException {
        unappropriatedContentService.create(
                UnappropriatedContentMapper.newToEntity(contentRequestDTO), principal.getName());
        return new ResponseEntity<>("Request is successfully saved.", HttpStatus.OK);
    }

    @PutMapping(path = "/approve/{requestId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> approve(@PathVariable Long requestId)
            throws ResourceNotFoundException, MessagingException {
        UnappropriatedContent content = unappropriatedContentService.approve(requestId);
        unappropriatedContentOrchestrator.startSaga(content, Constants.START_ACTION);
        return new ResponseEntity<>("Request is successfully approved.", HttpStatus.OK);
    }

    @PutMapping(path = "/reject/{requestId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> reject(@PathVariable Long requestId) throws ResourceNotFoundException {
        unappropriatedContentService.reject(requestId);
        return new ResponseEntity<>("Request is successfully rejceted.", HttpStatus.OK);
    }

    @GetMapping(path = "/requests")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<UnappropriatedContentResponseDTO>> getAllRequest() throws ResourceNotFoundException {
        List<UnappropriatedContentResponseDTO> retVal =
                unappropriatedContentService.findAllRequested()
                        .stream()
                        .map(UnappropriatedContentMapper::toDto)
                        .collect(Collectors.toList());
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }

}
