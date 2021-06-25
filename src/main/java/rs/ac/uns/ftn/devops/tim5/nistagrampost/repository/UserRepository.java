package rs.ac.uns.ftn.devops.tim5.nistagrampost.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
