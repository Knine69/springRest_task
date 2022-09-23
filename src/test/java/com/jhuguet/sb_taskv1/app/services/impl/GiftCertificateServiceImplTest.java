package com.jhuguet.sb_taskv1.app.services.impl;

import com.jhuguet.sb_taskv1.app.exceptions.IdNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.InvalidIdInputInformation;
import com.jhuguet.sb_taskv1.app.models.GiftCertificate;
import com.jhuguet.sb_taskv1.app.repositories.GiftCertificateRepository;
import com.jhuguet.sb_taskv1.app.repositories.TagRepository;
import com.jhuguet.sb_taskv1.app.services.utils.SetUpUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;

@SpringBootTest(classes = GiftCertificateServiceImplTest.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GiftCertificateServiceImplTest {

    private static SetUpUtils utils = new SetUpUtils();
    static GiftCertificateRepository giftCertificateRepository = Mockito.mock(GiftCertificateRepository.class);
    static TagRepository tagRepository = Mockito.mock(TagRepository.class);
    GiftCertificateServiceImpl service = new GiftCertificateServiceImpl(giftCertificateRepository, tagRepository);

    @BeforeAll
    private static void setMocks() {
        //Setting up mocks for giftCertificateRepository
        Mockito.when(giftCertificateRepository.existsById(anyInt())).thenReturn(true);
        Mockito.when(giftCertificateRepository.findById(anyInt()))
                .thenReturn(Optional.ofNullable(utils.sampleCertificate()));
        Mockito.when(giftCertificateRepository.findAll())
                .thenReturn(utils.sampleCertificates());

        //Setting up mocks for tagRepository
        Mockito.when(tagRepository.existsById(anyInt())).thenReturn(true);
        Mockito.when(tagRepository.findById(anyInt()))
                .thenReturn(Optional.ofNullable(utils.sampleTag()));
        Mockito.when(tagRepository.findAll())
                .thenReturn(utils.sampleTags());

    }


    @Test
    void getAll() {
    }

    @Test
    void get() throws IdNotFound, InvalidIdInputInformation {
        GiftCertificate certificate = utils.sampleCertificate();
        Assertions.assertEquals(certificate.getName(), service.get(anyInt()).getName());
    }

    @Test
    void save() {
    }

    @Test
    void update() {
    }

    @Test
    void updateTags() {
    }

    @Test
    void getByTagName() {
    }

    @Test
    void getByPart() {
    }

    @Test
    void getByDateOrName() {
    }

    @Test
    void delete() {
    }
}