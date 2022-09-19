package com.jhuguet.sb_taskv1.app.repositories;

import com.jhuguet.sb_taskv1.app.models.GiftCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftCertificateRepository extends JpaRepository<GiftCertificate, Integer> {
}
