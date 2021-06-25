package rs.ac.uns.ftn.devops.tim5.nistagrampost.service;

import rs.ac.uns.ftn.devops.tim5.nistagrampost.exception.ResourceNotFoundException;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.User;

public interface UserService {

    User findByUsername(String username) throws ResourceNotFoundException;
    
    void create(String username, String email);

    void delete(String username);
}
