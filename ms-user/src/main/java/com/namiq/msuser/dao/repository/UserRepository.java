package com.namiq.msuser.dao.repository;

import com.namiq.msuser.dao.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByUsername(String username);
    @Query("""
       SELECT u FROM User u
       WHERE u.lastLoginTime IS NULL
          OR u.lastLoginTime < :date
       """)
    List<User> findInactiveUsers(@Param("date") LocalDateTime date);
    boolean existsByEmail(String email);
}