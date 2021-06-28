package rs.ac.uns.ftn.devops.tim5.nistagrampost.service;

import rs.ac.uns.ftn.devops.tim5.nistagrampost.exception.ResourceNotFoundException;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.UnappropriatedContent;

import javax.mail.MessagingException;
import java.util.Collection;

public interface UnappropriatedContentService {

    UnappropriatedContent findById(Long id) throws ResourceNotFoundException;
    UnappropriatedContent create(UnappropriatedContent content, String username) throws ResourceNotFoundException;
    UnappropriatedContent approve(Long id) throws ResourceNotFoundException, MessagingException;
    UnappropriatedContent reject(Long id) throws ResourceNotFoundException;
    Collection<UnappropriatedContent> findAllRequested() throws ResourceNotFoundException;
    void delete(Long id) throws ResourceNotFoundException;
}
