package com.jhuguet.sb_taskv1.app.services.impl;

import com.google.gson.JsonParser;
import com.jhuguet.sb_taskv1.app.exceptions.IdNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.InvalidIdInputInformation;
import com.jhuguet.sb_taskv1.app.exceptions.MissingEntity;
import com.jhuguet.sb_taskv1.app.models.GiftCertificate;
import com.jhuguet.sb_taskv1.app.repositories.GiftCertificateRepository;
import com.jhuguet.sb_taskv1.app.repositories.TagRepository;
import com.jhuguet.sb_taskv1.app.services.utils.SetUpUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = GiftCertificateServiceImplTest.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GiftCertificateServiceImplTest {

    private static final SetUpUtils utils = new SetUpUtils();
    static final GiftCertificateRepository giftCertificateRepository = Mockito.mock(GiftCertificateRepository.class);
    static final TagRepository tagRepository = Mockito.mock(TagRepository.class);
    GiftCertificateServiceImpl giftCertificateService = new GiftCertificateServiceImpl(giftCertificateRepository, tagRepository);

    @BeforeAll
    private void setMocks() {
        //Setting up mocks for giftCertificateRepository
        when(giftCertificateRepository.existsById(anyInt())).thenReturn(true);
        when(giftCertificateRepository.findById(0))
                .thenReturn(Optional.ofNullable(utils.sampleCertificate()));
        when(giftCertificateRepository.findAll())
                .thenReturn(utils.sampleCertificates());

        //Setting up mocks for tagRepository
        when(tagRepository.existsById(anyInt())).thenReturn(true);
        when(tagRepository.findById(0))
                .thenReturn(Optional.ofNullable(utils.sampleTag()));
        when(tagRepository.findAll())
                .thenReturn(new ArrayList<>(utils.sampleTags()));

    }


    @Test
    void checkAllCertificatesFromRepositoryAreReturnedSuccessfully() {
        assertEquals(giftCertificateService.getAll().size(), utils.sampleCertificates().size());
    }

    @Test
    void getIDFound() throws IdNotFound {
        GiftCertificate certificate = utils.sampleCertificate();
        assertEquals(certificate.getName(), giftCertificateService.get(anyInt()).getName());
    }

    @Test
    void getIDNotFound() throws IdNotFound {
        assertThrows(IdNotFound.class, () -> {
            GiftCertificate certificate = giftCertificateService.get(-1);
        });
    }

    @Test
    void saveNotMissingEntity() throws MissingEntity {
        GiftCertificate certificate = utils.sampleCertificate();
        assertEquals(giftCertificateService.save(certificate), certificate);
    }

    @Test
    void saveMissingEntity() {
        assertThrows(MissingEntity.class, () -> giftCertificateService.save(null));
    }

    @Test
    void updateEqualsZero() throws IdNotFound, InvalidIdInputInformation {
        Map<String, Object> patchTest = Map.ofEntries(
                entry("name", "AWS Certificate"),
                entry("description", "AWS Master's certificate"),
                entry("price", 8.99),
                entry("duration", 10),
                entry("lastUpdateDate", "2022-09-20T14:33:15.1301054"),
                entry("associatedTags", JsonParser.parseString("[{\"name\":\"Cloud\"}]")),
                entry("", "")
        );
        assertEquals(giftCertificateService.update(0, patchTest).getName(), utils.sampleCertificate().getName());
    }

    @Test
    void updateIDDiffThanZero() throws IdNotFound, InvalidIdInputInformation {
        Map<String, Object> patchTest = Map.ofEntries(
                entry("name", "AWS Certificate"),
                entry("description", "AWS Master's certificate"),
                entry("price", 8.99),
                entry("duration", 10),
                entry("lastUpdateDate", "2022-09-20T14:33:15.1301054"),
                entry("associatedTags", JsonParser.parseString("[{\"id\":\"1\",\"name\":\"Cloud\"}]")),
                entry("", "")
        );
        assertEquals(giftCertificateService.update(0, patchTest).getName(), utils.sampleCertificate().getName());
    }

//    [{id=2, name=Cloud}]

    @Test
    void updateIDNotFound() throws IdNotFound, InvalidIdInputInformation {
        Map<String, Object> patchTest = Map.ofEntries(
                entry("name", "AWS Certificate"),
                entry("description", "AWS Master's certificate"),
                entry("price", 8.99),
                entry("duration", 10),
                entry("lastUpdateDate", "2022-09-20T14:33:15.1301054"),
                entry("", "")
        );
        when(giftCertificateRepository.existsById(anyInt())).thenReturn(false);
        assertThrows(IdNotFound.class, () -> giftCertificateService.update(anyInt(), patchTest));
        when(giftCertificateRepository.existsById(anyInt())).thenReturn(true);
    }


    @Test
    void updateInvalidIdInput() throws IdNotFound, InvalidIdInputInformation {
        Map<String, Object> patchTest = Map.ofEntries(
                entry("name", "AWS Certificate"),
                entry("description", "AWS Master's certificate"),
                entry("price", 8.99),
                entry("duration", 10),
                entry("lastUpdateDate", "2022-09-20T14:33:15.1301054"),
                entry("", "")
        );
        assertThrows(InvalidIdInputInformation.class, () -> giftCertificateService.update(-1, patchTest));
    }

    @Test
    void getByTagName() {
        assertEquals(giftCertificateService.getByTagName("Cloud").size(), utils.sampleCertificates().size());
    }

    @Test
    void getByPart() {
        assertEquals(giftCertificateService.getByPart("Master").size(), utils.sampleCertificates().size());
    }

    @Test
    void getByDateOrNameByNameAsc() {
        assertEquals(giftCertificateService.getByDateOrName("name", "asc").get(0).getName(), utils.sampleCertificates().get(0).getName());
    }

    @Test
    void getByDateOrNameByDateAsc() {
        assertEquals(giftCertificateService.getByDateOrName("lastUpdateDate", "asc").get(0).getName(), utils.sampleCertificates().get(0).getName());
    }

    @Test
    void getByDateOrNameDefaultCaseAsc() {
        assertEquals(giftCertificateService.getByDateOrName("", "asc").size(), 0);
    }

    @Test
    void getByDateOrNameByNameDesc() {
        assertEquals(giftCertificateService.getByDateOrName("name", "desc").get(1).getName(), utils.sampleCertificates().get(0).getName());
    }

    @Test
    void getByDateOrNameByDateDesc() {
        assertEquals(giftCertificateService.getByDateOrName("createDate", "desc").get(1).getName(), utils.sampleCertificates().get(0).getName());
    }

    @Test
    void getByDateOrNameDefaultCaseDesc() {
        assertEquals(giftCertificateService.getByDateOrName("", "desc").size(), 0);
    }

    @Test
    void delete() throws IdNotFound, InvalidIdInputInformation {
        assertEquals(giftCertificateService.delete(anyInt()).getName(), utils.sampleCertificate().getName());
    }
}