package com.jhuguet.sb_taskv1.app.services.impl;

import com.google.gson.JsonParser;
import com.jhuguet.sb_taskv1.app.exceptions.IdNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.InvalidInputInformation;
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
    void getIDNotFound() {
        assertThrows(IdNotFound.class, () -> {
            GiftCertificate certificate = giftCertificateService.get(-1);
        });
    }

    @Test
    void filterCertificatesByTagName() {
        assertEquals(utils.sampleCertificates().size(),
                giftCertificateService.filterCertificates("Cloud", "", "", "").size());
    }

    @Test
    void filterCertificatesByPartOfNameOrDescription() {
        assertEquals(utils.sampleCertificates().size(),
                giftCertificateService.filterCertificates("", "Master", "", "").size());
    }

    @Test
    void filterCertificatesByCreateDate() {
        assertEquals(utils.sampleCertificates().size(),
                giftCertificateService.filterCertificates("", "", "createDate", "asc").size());
    }

    @Test
    void filterCertificatesByLastUpdateDate() {
        assertEquals(utils.sampleCertificates().size(),
                giftCertificateService.filterCertificates("", "", "lastUpdateDate", "asc").size());
    }

    @Test
    void filterCertificatesByName() {
        assertEquals(utils.sampleCertificates().size(),
                giftCertificateService.filterCertificates("", "", "name", "asc").size());
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
    void updateEqualsZero() throws IdNotFound, InvalidInputInformation {
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
    void updateIDDiffThanZero() throws IdNotFound, InvalidInputInformation {
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

    @Test
    void updateIDNotFound() {
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
    void updateInvalidIdInput() {
        Map<String, Object> patchTest = Map.ofEntries(
                entry("name", "AWS Certificate"),
                entry("description", "AWS Master's certificate"),
                entry("price", 8.99),
                entry("duration", 10),
                entry("lastUpdateDate", "2022-09-20T14:33:15.1301054"),
                entry("", "")
        );
        assertThrows(InvalidInputInformation.class, () -> giftCertificateService.update(-1, patchTest));
    }

    @Test
    void delete() throws IdNotFound {
        assertEquals(giftCertificateService.delete(anyInt()).getName(), utils.sampleCertificate().getName());
    }
}