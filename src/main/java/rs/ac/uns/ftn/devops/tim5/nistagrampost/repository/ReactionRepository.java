package rs.ac.uns.ftn.devops.tim5.nistagrampost.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.Reaction;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {
}
