package rs.ac.uns.ftn.devops.tim5.nistagrampost.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.Comment;

import java.util.Collection;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT r FROM Comment r WHERE r.post.id = ?1")
    Collection<Comment> findAllByPostId(Long postId);
}
