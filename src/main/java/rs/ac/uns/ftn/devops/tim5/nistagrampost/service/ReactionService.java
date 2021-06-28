package rs.ac.uns.ftn.devops.tim5.nistagrampost.service;

import rs.ac.uns.ftn.devops.tim5.nistagrampost.exception.ResourceNotFoundException;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.Reaction;

public interface ReactionService {

    Reaction create(Reaction reaction, String username) throws ResourceNotFoundException;
    Reaction update(Reaction reaction) throws ResourceNotFoundException;
    void delete(Long id) throws ResourceNotFoundException;
    Reaction findById(Long id) throws ResourceNotFoundException;
}
