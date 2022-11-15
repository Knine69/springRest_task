package com.jhuguet.sb_taskv1.app.repositories;

import com.jhuguet.sb_taskv1.app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query(value = "SELECT * FROM USERS u WHERE u.username = ?1")
    User findByUsername(String username);

}
