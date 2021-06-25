package rs.ac.uns.ftn.devops.tim5.nistagrampost.service;

import rs.ac.uns.ftn.devops.tim5.nistagrampost.exception.ResourceNotFoundException;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.Post;

public interface PostService {
    Post create(Post post, String username) throws ResourceNotFoundException;

    void delete(Long id);
}
