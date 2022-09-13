package com.jhuguet.sb_taskv1.repositories;

import com.jhuguet.sb_taskv1.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {
}
