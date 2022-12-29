package edu.kazakov.notes.repository;

import edu.kazakov.notes.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
    @Query("SELECT u FROM User u WHERE u.verificationCode = ?1")
    public User findByVerificationCode(String code);
}