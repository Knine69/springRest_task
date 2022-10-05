package com.jhuguet.sb_taskv1.app.repositories;

import com.jhuguet.sb_taskv1.app.models.GiftCertificate;
import com.jhuguet.sb_taskv1.app.services.utils.SetUpUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@TestPropertySource("classpath:application-dev.properties")
class GiftCertificateRepositoryTest {

    private SetUpUtils utils = new SetUpUtils();
    @Autowired
    private TestEntityManager em;
    @Autowired
    private GiftCertificateRepository repository;


    @Test
    public void contextLoads() {
        assertNotNull(em);
    }

    @Test
    public void save() {
        GiftCertificate certificate = utils.sampleCertificate();
        assertFalse(repository.existsById(certificate.getId()));
        repository.save(certificate);
        assertTrue(repository.existsById(certificate.getId()));
    }

    @Test
    public void delete() {
        GiftCertificate certificate = utils.sampleCertificate();
        repository.save(certificate);
        assertTrue(repository.existsById(certificate.getId()));
        repository.deleteById(certificate.getId());
        assertFalse(repository.existsById(certificate.getId()));
    }

    @Test
    public void update() {
        GiftCertificate certificate = utils.sampleCertificate();
        repository.save(certificate);
        assertTrue(repository.existsById(certificate.getId()));
        assertEquals(repository.findById(certificate.getId()).get().getLastUpdateDate(),
                "2022-09-20T14:33:15.1301054");
        certificate.setLastUpdateDate(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        repository.save(certificate);
        Assertions.assertNotEquals(repository.findById(certificate.getId()).get().getLastUpdateDate(),
                "2022-09-20T14:33:15.1301054");
    }

    @Test
    public void getAll() {
        List<GiftCertificate> certificates = repository.findAll();
        assertEquals(4, certificates.size());
        assertNotNull(certificates);
    }

    @Test
    public void get() {
        assertEquals("AWS", repository.findById(1).get().getName());
        assertNotNull(repository.findById(1).get().getName());
        assertThrows(InvalidDataAccessApiUsageException.class, () -> repository.findById(null));
    }

}