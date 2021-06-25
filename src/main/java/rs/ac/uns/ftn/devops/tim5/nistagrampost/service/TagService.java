package rs.ac.uns.ftn.devops.tim5.nistagrampost.service;

import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.Tag;

import java.util.Set;

public interface TagService {
    Set<Tag> getTagsForPost(Set<Tag> tags);
}
