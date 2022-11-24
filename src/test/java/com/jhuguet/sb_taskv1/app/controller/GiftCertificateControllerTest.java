package com.jhuguet.sb_taskv1.app.controller;

import com.jhuguet.sb_taskv1.app.exceptions.BaseException;
import com.jhuguet.sb_taskv1.app.exceptions.IdAlreadyInUse;
import com.jhuguet.sb_taskv1.app.exceptions.IdNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.InvalidInputInformation;
import com.jhuguet.sb_taskv1.app.exceptions.MissingEntity;
import com.jhuguet.sb_taskv1.app.exceptions.PageNotFound;
import com.jhuguet.sb_taskv1.app.models.GiftCertificate;
import com.jhuguet.sb_taskv1.app.pages.PageResponse;
import com.jhuguet.sb_taskv1.app.repositories.GiftCertificateRepository;
import com.jhuguet.sb_taskv1.app.services.UserService;
import com.jhuguet.sb_taskv1.app.services.impl.GiftCertificateServiceImpl;
import com.jhuguet.sb_taskv1.app.services.utils.SetUpUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@RunWith(MockitoJUnitRunner.class)
class GiftCertificateControllerTest {

    SetUpUtils utils = new SetUpUtils();
    @Mock
    PageResponse pageResponse = new PageResponse();
    @Mock
    UserService userService;
    @Mock
    GiftCertificateServiceImpl giftCertificateService;
    @Mock
    GiftCertificateRepository certificateRepository;

    @InjectMocks
    GiftCertificateController controller;

    @BeforeAll
    void mocksSetuUp() throws BaseException, IOException {
        MockitoAnnotations.openMocks(this);
        Mockito.when(pageResponse.giveDynamicPageable(1, 1, "asc")).thenReturn(PageRequest.of(1, 1));
        Mockito.when(giftCertificateService.delete(0)).thenReturn(utils.sampleCertificate());
        Mockito.when(giftCertificateService.delete(10)).thenThrow(IdNotFound.class);
        Mockito.when(giftCertificateService.placeNewOrder(new ArrayList<>(), 1)).thenReturn(utils.sampleOrder());
        Mockito.when(giftCertificateService.placeNewOrder(new ArrayList<>(), 10)).thenThrow(IdNotFound.class);
        Mockito.when(giftCertificateService.get(0)).thenReturn(utils.sampleCertificate());
        Mockito.when(giftCertificateService.save(Mockito.any(GiftCertificate.class))).thenReturn(
                utils.sampleCertificate());
        Mockito.when(giftCertificateService.save(null)).thenThrow(MissingEntity.class);
        Mockito.when(giftCertificateService.update(0, new HashMap<>())).thenReturn(utils.sampleCertificate());
        Mockito.when(giftCertificateService.update(1, new HashMap<>())).thenThrow(IdNotFound.class);
        Mockito.when(giftCertificateService.update(-1, new HashMap<>())).thenThrow(InvalidInputInformation.class);
        Mockito.when(giftCertificateService.getAllPageable(Mockito.any(Pageable.class))).thenReturn(
                utils.samplePageCertificates());
        Mockito.when(giftCertificateService.filterCertificates("", "", "", "", PageRequest.of(1, 1))).thenReturn(
                utils.samplePageCertificates());
        Mockito.when(giftCertificateService.filterCertificates("", "", "createDate", "", PageRequest.of(1, 1)))
               .thenReturn(utils.samplePageCertificates());
        Mockito.when(giftCertificateService.filterCertificates("", "", "createDate", "asc", PageRequest.of(1, 1)))
               .thenReturn(utils.samplePageCertificates());
        Mockito.when(giftCertificateService.filterCertificates("", "", "lastUpdateDate", "desc", PageRequest.of(1, 1)))
               .thenReturn(utils.samplePageCertificates());
        Mockito.when(giftCertificateService.filterCertificates("", "", "name", "asc", PageRequest.of(1, 1))).thenReturn(
                utils.samplePageCertificates());
        Mockito.when(giftCertificateService.filterCertificates("Cloud", "", "", "", PageRequest.of(1, 1))).thenReturn(
                utils.samplePageCertificates());
        Mockito.when(giftCertificateService.filterCertificates("", "Master", "", "", PageRequest.of(1, 1))).thenReturn(
                utils.samplePageCertificates());
        Mockito.when(userService.getIdFromJwt(Mockito.anyString())).thenReturn(1);
    }

    @Test
    void getAllGiftCertificatesIfCorrectlyGivenPagingParams() throws BaseException {
        Assertions.assertEquals(utils.sampleCertificates().size(),
                Objects.requireNonNull(controller.getAll(1, 1, "asc").getContent()).getTotalElements());
    }

    @Test
    void getOneSpecificGiftCertificateIfCorrectlyGivenId() throws IdNotFound, InvalidInputInformation {
        Assertions.assertEquals(utils.sampleCertificate().getName(), controller.get(0).getName());
    }

    @Test
    void getGiftCertificateByTagName() throws InvalidInputInformation, PageNotFound {
        Assertions.assertEquals(utils.sampleCertificates().size(),
                Objects.requireNonNull(controller.getBy("", "Cloud", "", "", 1, 1).getContent()).getTotalElements());
    }

    @Test
    void getGiftCertificateByPartOfNameOrDescription() throws InvalidInputInformation, PageNotFound {
        Assertions.assertEquals(utils.sampleCertificates().size(),
                Objects.requireNonNull(controller.getBy("Master", "", "", "", 1, 1).getContent()).getTotalElements());
    }

    @Test
    void getByReturnsNoGiftCertificate() throws InvalidInputInformation, PageNotFound {
        Assertions.assertEquals(utils.sampleCertificates().size(),
                Objects.requireNonNull(controller.getBy("", "", "", "", 1, 1).getContent()).getTotalElements());
    }

    @Test
    void getGiftCertificateByCreatedDate() throws InvalidInputInformation, PageNotFound {
        Assertions.assertEquals(utils.sampleCertificates().size(),
                Objects.requireNonNull(controller.getBy("", "", "createDate", "asc", 1, 1).getContent())
                       .getTotalElements());
    }

    @Test
    void getGiftCertificateByLastUpdatedDateReverseOrder() throws InvalidInputInformation, PageNotFound {
        Assertions.assertEquals(utils.sampleCertificates().size(),
                Objects.requireNonNull(controller.getBy("", "", "lastUpdateDate", "desc", 1, 1).getContent())
                       .getTotalElements());
    }

    @Test
    void getGiftCertificateByName() throws InvalidInputInformation, PageNotFound {
        Assertions.assertEquals(utils.sampleCertificates().size(),
                Objects.requireNonNull(controller.getBy("", "", "name", "asc", 1, 1).getContent()).getTotalElements());
    }

    @Test
    void getGiftCertificateByNameOrDateIncorrectOrder() throws InvalidInputInformation, PageNotFound {
        Assertions.assertEquals(utils.sampleCertificates().size(),
                Objects.requireNonNull(controller.getBy("", "", "createDate", "", 1, 1).getContent())
                       .getTotalElements());
    }

    @Test
    void saveGiftCertificateCorrectly() throws MissingEntity, InvalidInputInformation, IdAlreadyInUse {
        Assertions.assertEquals(utils.sampleCertificate().getName(),
                giftCertificateService.save(utils.sampleCertificate()).getName());
    }

    @Test
    void saveGiftCertificateMissingEntity() throws InvalidInputInformation, MissingEntity, IdAlreadyInUse {
        Assertions.assertThrows(MissingEntity.class, () -> controller.save(null));
    }

    @Test
    void updateGiftCertificateCorrectly() throws IdNotFound, InvalidInputInformation {
        Assertions.assertEquals("AWS Certificate", controller.update(0, new HashMap<>()).getName());
    }


    @Test
    void updateGiftCertificateIdNotFound() {
        Assertions.assertThrows(IdNotFound.class, () -> controller.update(1, new HashMap<>()).getName());
    }


    @Test
    void updateGiftCertificateInvalidInputNegativeId() {
        Assertions.assertThrows(InvalidInputInformation.class, () -> controller.update(-1, new HashMap<>()).getName());
    }

    @Test
    void deleteGiftCertificateCorrectly() throws IdNotFound {
        Assertions.assertEquals("AWS Certificate", controller.delete(0).getName());
    }

    @Test
    void deleteGiftCertificateIdNotFound() {
        Assertions.assertThrows(IdNotFound.class, () -> controller.delete(10).getName());
    }

    @Test
    void placeNewOrderCorrectlyIfIdExists() throws BaseException, IOException {
        Assertions.assertEquals(utils.sampleOrder().getId(),
                controller.placeNewOrder(new ArrayList<>(), new HashMap<>(Map.of("authorization", "Bearer abcd")))
                          .getId());
    }

    @Test
    void placeNewOrderExceptionIfIdNotFound() throws IOException {
        Mockito.when(userService.getIdFromJwt(Mockito.anyString())).thenReturn(10);
        Assertions.assertThrows(IdNotFound.class, () -> controller.placeNewOrder(new ArrayList<>(List.of()),
                new HashMap<>(Map.of("authorization", "Bearer abcd"))));
    }

}
