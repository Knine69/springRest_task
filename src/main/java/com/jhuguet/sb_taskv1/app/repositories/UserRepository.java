package com.jhuguet.sb_taskv1.app.repositories;

import com.jhuguet.sb_taskv1.app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);

}
