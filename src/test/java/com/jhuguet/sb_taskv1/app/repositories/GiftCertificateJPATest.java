package com.jhuguet.sb_taskv1.app.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Profile("dev")
@ContextConfiguration(classes = {GiftCertificateRepository.class, TestEntityManager.class})
public class GiftCertificateJPATest {
    @Autowired
    private GiftCertificateRepository certificateRepository;

    @Test
    void getAllRepositoryEmpty() {
        Iterable certificates = certificateRepository.findAll();
        assertThat(certificates).isEmpty();
    }
}
