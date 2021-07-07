package rs.ac.uns.ftn.devops.tim5.nistagrampost.mapper;

import rs.ac.uns.ftn.devops.tim5.nistagrampost.dto.TagDTO;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.Tag;

public class TagMapper {
    private TagMapper() {
    }

    public static Tag toEntity(TagDTO tag) {
        return new Tag(tag.getTitle());
    }
}