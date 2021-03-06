package rs.ac.uns.ftn.devops.tim5.nistagrampost.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.Tag;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.repository.TagRepository;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.service.TagService;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public Set<Tag> getTagsForPost(Set<Tag> tags) {
        HashSet<Tag> tagsSet = new HashSet<>();
        for (Tag tag : tags) {
            Optional<Tag> optionalTag = tagRepository.findByTitle(tag.getTitle());
            Tag newTag = optionalTag.orElseGet(() -> tagRepository.save(new Tag(null, tag.getTitle())));
            tagsSet.add(newTag);
        }
        return tagsSet;
    }
}
