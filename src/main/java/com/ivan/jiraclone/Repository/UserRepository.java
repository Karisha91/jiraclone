package com.ivan.jiraclone.Repository;

import com.ivan.jiraclone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 5# method that checks credentials of user in database
    Optional<User> findByUsername(String username);

}
