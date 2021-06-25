package rs.ac.uns.ftn.devops.tim5.nistagrampost.service.impl;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.exception.ResourceNotFoundException;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.User;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.repository.UserRepository;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findByUsername(String username) throws ResourceNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User"));
    }
}
