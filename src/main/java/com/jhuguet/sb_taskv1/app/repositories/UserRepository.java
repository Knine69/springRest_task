package com.jhuguet.sb_taskv1.app.repositories;

import com.jhuguet.sb_taskv1.app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
