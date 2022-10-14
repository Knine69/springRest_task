package com.jhuguet.sb_taskv1.app.controller;

import com.jhuguet.sb_taskv1.app.exceptions.IdNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.InvalidInputInformation;
import com.jhuguet.sb_taskv1.app.exceptions.MissingEntity;
import com.jhuguet.sb_taskv1.app.models.GiftCertificate;
import com.jhuguet.sb_taskv1.app.repositories.GiftCertificateRepository;
import com.jhuguet.sb_taskv1.app.repositories.TagRepository;
import com.jhuguet.sb_taskv1.app.services.impl.GiftCertificateServiceImpl;
import com.jhuguet.sb_taskv1.app.services.utils.SetUpUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Optional;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class GiftCertificateControllerTest {

    private static final SetUpUtils utils = new SetUpUtils();
    private static final GiftCertificateRepository giftCertificateRepository = Mockito.mock(GiftCertificateRepository.class);
    private static final TagRepository tagRepository = Mockito.mock(TagRepository.class);
    @Autowired
    private static final GiftCertificateServiceImpl giftCertificateService = new GiftCertificateServiceImpl(giftCertificateRepository, tagRepository);

    private final GiftCertificateController controller = new GiftCertificateController(giftCertificateService);

    @BeforeAll
    private static void mocksSetuUp() {
        when(giftCertificateRepository.existsById(anyInt())).thenReturn(true);
        when(giftCertificateRepository.findById(0))
                .thenReturn(Optional.ofNullable(utils.sampleCertificate()));
        when(giftCertificateRepository.findAll())
                .thenReturn(utils.sampleCertificates());
        when(giftCertificateRepository.save(new GiftCertificate())).thenReturn(utils.sampleCertificate());
    }

    @Test
    void getReturnOne() throws IdNotFound, InvalidInputInformation {
        assertEquals(utils.sampleCertificate().getName(), controller.get("0").getName());
    }

    @Test
    void getReturnAll() throws IdNotFound, InvalidInputInformation {
        assertEquals(utils.sampleCertificates().size(), controller.getAll().size());
    }

    @Test
    void getByTagName() {
        assertEquals(utils.sampleCertificates().size(),
                controller.getBy("", "Cloud", "", "").size());
    }

    @Test
    void getByPartOfNameOrDescription() {
        assertEquals(utils.sampleCertificates().size(),
                controller.getBy("Master", "", "", "").size());
    }

    @Test
    void getByEmpty() {
        assertEquals(utils.sampleCertificates().size(),
                controller.getBy("", "", "", "").size());
    }

    @Test
    void getByCreatedDate() {
        assertEquals(utils.sampleCertificates().size(),
                controller.getBy("", "", "createDate", "asc").size());
    }

    @Test
    void getByLastUpdatedDateReverseOrder() {
        assertEquals(utils.sampleCertificates().size(),
                controller.getBy("", "", "lastUpdateDate", "desc").size());
    }

    @Test
    void getByName() {
        assertEquals(utils.sampleCertificates().size(),
                controller.getBy("", "", "name", "asc").size());
    }

    @Test
    void getByNameOrDateIncorrectOrder() {
        assertEquals(0,
                controller.getBy("", "", "createDate", "").size());
    }

    @Test
    void save() throws MissingEntity {
        assertEquals(utils.sampleCertificate().getName(), giftCertificateService.save(utils.sampleCertificate()).getName());
    }

    @Test
    void saveMissingEntity() {
        assertThrows(MissingEntity.class, () -> controller.save(null));
    }

    @Test
    void updateCorrectly() throws IdNotFound, InvalidInputInformation {
        Map<String, Object> patchTest = Map.ofEntries(
                entry("name", "AWS Certificate"),
                entry("description", "AWS Master's certificate"),
                entry("price", 8.99),
                entry("duration", 10),
                entry("lastUpdateDate", "2022-09-20T14:33:15.1301054"),
                entry("", "")
        );
        assertEquals("AWS Certificate", controller.update("0", patchTest).getName());
    }

    @Test
    void updatePriceDurationIncorrectValues() throws IdNotFound, InvalidInputInformation {
        Map<String, Object> patchTest = Map.ofEntries(
                entry("name", "AWS Certificate"),
                entry("description", "AWS Master's certificate"),
                entry("price", -8.99),
                entry("duration", -10),
                entry("lastUpdateDate", "2022-09-20T14:33:15.1301054"),
                entry("", "")
        );
        assertEquals("AWS Certificate", controller.update("0", patchTest).getName());
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
        assertThrows(IdNotFound.class, () -> controller.update("1", patchTest).getName());
    }


    @Test
    void updateInvalidInput() {
        Map<String, Object> patchTest = Map.ofEntries(
                entry("name", "AWS Certificate"),
                entry("description", "AWS Master's certificate"),
                entry("price", 8.99),
                entry("duration", 10),
                entry("lastUpdateDate", "2022-09-20T14:33:15.1301054"),
                entry("", "")
        );
        assertThrows(InvalidInputInformation.class, () -> controller.update("-1", patchTest).getName());
    }

    @Test
    void deleteCorrectly() throws IdNotFound {
        assertEquals("AWS Certificate", controller.delete("0").getName());
    }

    @Test
    void deleteIDNotFound () {
        assertThrows(IdNotFound.class, () -> controller.delete("1").getName());
    }

}