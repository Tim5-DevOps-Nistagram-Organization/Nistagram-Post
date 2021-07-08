package rs.ac.uns.ftn.devops.tim5.nistagrampost.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.UnappropriatedContent;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.enums.UnappropriatedContentState;

import java.util.Collection;


@Repository
public interface UnappropriatedContentRepository extends JpaRepository<UnappropriatedContent, Long> {

    @Query("SELECT r FROM UnappropriatedContent r WHERE r.state = ?1")
    Collection<UnappropriatedContent> findAllByState(UnappropriatedContentState state);
}
