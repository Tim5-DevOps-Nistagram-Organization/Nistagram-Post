package rs.ac.uns.ftn.devops.tim5.nistagrampost.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.exception.ResourceNotFoundException;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.Reaction;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.repository.ReactionRepository;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.service.PostService;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.service.ReactionService;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.service.UserService;

import java.util.Collection;

@Service
public class ReactionServiceImpl implements ReactionService {

    private final PostService postService;
    private final UserService userService;
    private final ReactionRepository reactionRepository;


    @Autowired
    public ReactionServiceImpl(ReactionRepository reactionRepository,
                               PostService postService,
                               UserService userService) {
        this.reactionRepository = reactionRepository;
        this.postService = postService;
        this.userService = userService;
    }

    @Override
    public Reaction create(Reaction reaction, String username) throws ResourceNotFoundException {
        reaction.setUser(userService.findByUsername(username));
        reaction.setPost(postService.findById(reaction.getPost().getId()));
        return reactionRepository.save(reaction);
    }

    @Override
    public Reaction update(Reaction reaction) throws ResourceNotFoundException {
        Reaction found = this.findById(reaction.getId());
        found.setReaction(reaction.getReaction());
        return reactionRepository.save(found);
    }


    @Override
    public void delete(Long id) throws ResourceNotFoundException {
        Reaction reaction = this.findById(id);
        reactionRepository.delete(reaction);
    }

    @Override
    public Reaction findById(Long id) throws ResourceNotFoundException {
        return reactionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Reaction"));
    }

    @Override
    public Collection<Reaction> findAllByPostId(Long id) {
        return reactionRepository.findAllByPostId(id);
    }

    @Override
    public Reaction findByPostIdAndUserUsername(Long id, String username) {
        return reactionRepository.findByPost_IdAndUser_Username(id, username).orElse(null);
    }
}
