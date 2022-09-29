package com.jhuguet.sb_taskv1.app.services.impl;

import com.jhuguet.sb_taskv1.app.exceptions.IdNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.InvalidIdInputInformation;
import com.jhuguet.sb_taskv1.app.models.GiftCertificate;
import com.jhuguet.sb_taskv1.app.repositories.GiftCertificateRepository;
import com.jhuguet.sb_taskv1.app.repositories.TagRepository;
import com.jhuguet.sb_taskv1.app.services.utils.SetUpUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;
import java.util.Optional;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = GiftCertificateServiceImplTest.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GiftCertificateServiceImplTest {

    private static final SetUpUtils utils = new SetUpUtils();
    static final GiftCertificateRepository giftCertificateRepository = Mockito.mock(GiftCertificateRepository.class);
    static final TagRepository tagRepository = Mockito.mock(TagRepository.class);
    GiftCertificateServiceImpl service = new GiftCertificateServiceImpl(giftCertificateRepository, tagRepository);

    @BeforeAll
    private static void setMocks() {
        //Setting up mocks for giftCertificateRepository
        when(giftCertificateRepository.existsById(anyInt())).thenReturn(true);
        when(giftCertificateRepository.findById(anyInt()))
                .thenReturn(Optional.ofNullable(utils.sampleCertificate()));
        when(giftCertificateRepository.findAll())
                .thenReturn(utils.sampleCertificates());

        //Setting up mocks for tagRepository
        when(tagRepository.existsById(anyInt())).thenReturn(true);
        when(tagRepository.findById(anyInt()))
                .thenReturn(Optional.ofNullable(utils.sampleTag()));
        when(tagRepository.findAll())
                .thenReturn(utils.sampleTags());

    }


    @Test
    void getAll() {
        assertEquals(service.getAll().size(), utils.sampleCertificates().size());
    }

    @Test
    void get() throws IdNotFound, InvalidIdInputInformation {
        GiftCertificate certificate = utils.sampleCertificate();
        assertThrows(InvalidIdInputInformation.class, () -> service.get(-1));
        assertThrows(IdNotFound.class, () -> {
            when(giftCertificateRepository.existsById(anyInt())).thenReturn(false);
            service.get(1);
        });
        when(giftCertificateRepository.existsById(anyInt())).thenReturn(true);
        assertEquals(certificate.getName(), service.get(anyInt()).getName());
    }

    @Test
    void save() {
        GiftCertificate certificate = utils.sampleCertificate();
        assertThrows(NullPointerException.class, () -> service.save(null));
        assertEquals(service.save(certificate), certificate);
    }

    @Test
    void update() throws IdNotFound, InvalidIdInputInformation {
        Map<String, Object> patchTest = Map.ofEntries(
                entry("name", "AWS Certificate"),
                entry("description", "AWS Master's certificate"),
                entry("price", 8.99),
                entry("duration", 10),
                entry("lastUpdateDate", "2022-09-20T14:33:15.1301054"),
                entry("", "")
        );
        assertEquals(service.update(anyInt(), patchTest).getName(), utils.sampleCertificate().getName());
    }

    @Test
    void updateTags() throws InvalidIdInputInformation {
    }

    @Test
    void getByTagName() {
        assertEquals(service.getByTagName("Amazon").size(), utils.sampleCertificates().size());
    }

    @Test
    void getByPart() {
        assertEquals(service.getByPart("Master").size(), utils.sampleCertificates().size());
    }

    @Test
    void getByDateOrNameByNameAsc() {
        assertEquals(service.getByDateOrName("name", "asc").get(0).getName(), utils.sampleCertificates().get(0).getName());
    }

    @Test
    void getByDateOrNameByDateAsc() {
        assertEquals(service.getByDateOrName("date", "asc").get(0).getName(), utils.sampleCertificates().get(0).getName());
    }

    @Test
    void getByDateOrNameDefaultCaseAsc() {
        assertEquals(service.getByDateOrName("", "asc").size(), 0);
    }

    @Test
    void getByDateOrNameByNameDesc() {
        assertEquals(service.getByDateOrName("name", "desc").get(1).getName(), utils.sampleCertificates().get(0).getName());
    }

    @Test
    void getByDateOrNameByDateDesc() {
        assertEquals(service.getByDateOrName("date", "desc").get(1).getName(), utils.sampleCertificates().get(0).getName());
    }

    @Test
    void getByDateOrNameDefaultCaseDesc() {
        assertEquals(service.getByDateOrName("as", "desc").size(), 0);
    }

    @Test
    void delete() throws IdNotFound, InvalidIdInputInformation {
        assertEquals(service.delete(anyInt()).getName(), utils.sampleCertificate().getName());
    }
}