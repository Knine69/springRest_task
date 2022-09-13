package com.jhuguet.sb_taskv1.repositories;

import com.jhuguet.sb_taskv1.models.GiftCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftCertificateRepository extends JpaRepository<GiftCertificate, Integer> {
}
