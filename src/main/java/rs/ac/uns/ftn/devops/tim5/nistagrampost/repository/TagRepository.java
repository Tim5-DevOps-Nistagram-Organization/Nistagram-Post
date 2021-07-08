package rs.ac.uns.ftn.devops.tim5.nistagrampost.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.Tag;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByTitle(String title);
}
