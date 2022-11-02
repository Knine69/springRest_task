package com.jhuguet.sb_taskv1.app.controller;

import com.jhuguet.sb_taskv1.app.exceptions.IdAlreadyInUse;
import com.jhuguet.sb_taskv1.app.exceptions.IdNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.InvalidInputInformation;
import com.jhuguet.sb_taskv1.app.exceptions.MissingEntity;
import com.jhuguet.sb_taskv1.app.exceptions.PageNotFound;
import com.jhuguet.sb_taskv1.app.models.GiftCertificate;
import com.jhuguet.sb_taskv1.app.pages.PageResponse;
import com.jhuguet.sb_taskv1.app.repositories.GiftCertificateRepository;
import com.jhuguet.sb_taskv1.app.repositories.OrderRepository;
import com.jhuguet.sb_taskv1.app.repositories.TagRepository;
import com.jhuguet.sb_taskv1.app.repositories.UserRepository;
import com.jhuguet.sb_taskv1.app.services.impl.GiftCertificateServiceImpl;
import com.jhuguet.sb_taskv1.app.services.utils.SetUpUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class GiftCertificateControllerTest {

    private static final SetUpUtils utils = new SetUpUtils();
    private static final GiftCertificateRepository giftCertificateRepository = Mockito.mock(GiftCertificateRepository.class);
    private static final TagRepository tagRepository = Mockito.mock(TagRepository.class);
    private static final OrderRepository orderRepository = Mockito.mock(OrderRepository.class);
    private static final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private static final PageResponse pageResponse = Mockito.mock(PageResponse.class);
    @Autowired
    private static final GiftCertificateServiceImpl giftCertificateService =
            new GiftCertificateServiceImpl(giftCertificateRepository, tagRepository, userRepository, orderRepository);

    private final GiftCertificateController controller = new GiftCertificateController(pageResponse, giftCertificateService);

    @BeforeAll
    private static void mocksSetuUp() {
        when(giftCertificateRepository.existsById(anyInt())).thenReturn(true);
        when(giftCertificateRepository.findById(0))
                .thenReturn(Optional.ofNullable(utils.sampleCertificate()));
        when(giftCertificateRepository.save(new GiftCertificate())).thenReturn(utils.sampleCertificate());
        when(giftCertificateRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(
                        new ArrayList<>(utils.sampleCertificates()), PageRequest.of(1, 1), utils.sampleCertificates().size()
                ));
        when(pageResponse.giveDynamicPageable(anyInt(), anyInt(), anyString())).thenReturn(PageRequest.of(1, 1));
        when(tagRepository.findById(anyInt())).thenReturn(Optional.ofNullable(utils.sampleTag()));
    }

    @Test
    void getAllGiftCertificatesIfCorrectlyGivenPagingParams() throws InvalidInputInformation, IdNotFound, PageNotFound {
        assertEquals(utils.sampleCertificates().size(),
                controller.getAll(1, 1, "asc").getContent().getTotalElements());
    }

    @Test
    void getOneSpecificGiftCertificateIfCorrectlyGivenId() throws IdNotFound, InvalidInputInformation {
        assertEquals(utils.sampleCertificate().getName(), controller.get(0).getName());
    }

    @Test
    void getGiftCertificateByTagName() throws InvalidInputInformation, PageNotFound {
        assertEquals(utils.sampleCertificates().size(),
                controller.getBy("", "Cloud", "", "", 1, 1)
                        .getContent().getTotalElements());
    }

    @Test
    void getGiftCertificateByPartOfNameOrDescription() throws InvalidInputInformation, PageNotFound {
        assertEquals(utils.sampleCertificates().size(),
                controller.getBy("Master", "", "", "", 1, 1)
                        .getContent().getTotalElements());
    }

    @Test
    void getByReturnsNoGiftCertificate() throws InvalidInputInformation, PageNotFound {
        assertEquals(utils.sampleCertificates().size(),
                controller.getBy("", "", "", "", 1, 1)
                        .getContent().getTotalElements());
    }

    @Test
    void getGiftCertificateByCreatedDate() throws InvalidInputInformation, PageNotFound {
        assertEquals(utils.sampleCertificates().size(),
                controller.getBy("", "", "createDate", "asc", 1, 1)
                        .getContent().getTotalElements());
    }

    @Test
    void getGiftCertificateByLastUpdatedDateReverseOrder() throws InvalidInputInformation, PageNotFound {
        assertEquals(utils.sampleCertificates().size(),
                controller.getBy("", "", "lastUpdateDate", "desc", 1, 1)
                        .getContent().getTotalElements());
    }

    @Test
    void getGiftCertificateByName() throws InvalidInputInformation, PageNotFound {
        assertEquals(utils.sampleCertificates().size(),
                controller.getBy("", "", "name", "asc", 1, 1)
                        .getContent().getTotalElements());
    }

    @Test
    void getGiftCertificateByNameOrDateIncorrectOrder() throws InvalidInputInformation, PageNotFound {
        assertEquals(0,
                controller.getBy("", "", "createDate", "", 1, 1)
                        .getContent().getTotalElements());
    }

    @Test
    void saveGiftCertificateCorrectly() throws MissingEntity, InvalidInputInformation, IdAlreadyInUse {
        assertEquals(utils.sampleCertificate().getName(), giftCertificateService.save(utils.sampleCertificate()).getName());
    }

    @Test
    void saveGiftCertificateMissingEntity() {
        assertThrows(MissingEntity.class, () -> controller.save(null));
    }

    @Test
    void updateGiftCertificateCorrectly() throws IdNotFound, InvalidInputInformation {
        Map<String, Object> patchTest = Map.ofEntries(
                entry("name", "AWS Certificate"),
                entry("description", "AWS Master's certificate"),
                entry("price", 8.99),
                entry("duration", 10),
                entry("lastUpdateDate", "2022-09-20T14:33:15.1301054"),
                entry("", "")
        );
        assertEquals("AWS Certificate", controller.update(0, patchTest).getName());
    }

    @Test
    void updateGiftCertificatePriceDurationIncorrectValues() {
        Map<String, Object> patchTest = Map.ofEntries(
                entry("name", "AWS Certificate"),
                entry("description", "AWS Master's certificate"),
                entry("price", -8.99),
                entry("duration", -10),
                entry("lastUpdateDate", "2022-09-20T14:33:15.1301054"),
                entry("", "")
        );
        assertThrows(InvalidInputInformation.class, () -> controller.update(0, patchTest).getName());
    }

    @Test
    void updateGiftCertificateIDNotFound() {
        Map<String, Object> patchTest = Map.ofEntries(
                entry("name", "AWS Certificate"),
                entry("description", "AWS Master's certificate"),
                entry("price", 8.99),
                entry("duration", 10),
                entry("lastUpdateDate", "2022-09-20T14:33:15.1301054"),
                entry("", "")
        );
        assertThrows(IdNotFound.class, () -> controller.update(1, patchTest).getName());
    }


    @Test
    void updateGiftCertificateInvalidInputNegativeId() {
        Map<String, Object> patchTest = Map.ofEntries(
                entry("name", "AWS Certificate"),
                entry("description", "AWS Master's certificate"),
                entry("price", 8.99),
                entry("duration", 10),
                entry("lastUpdateDate", "2022-09-20T14:33:15.1301054"),
                entry("", "")
        );
        assertThrows(InvalidInputInformation.class, () -> controller.update(-1, patchTest).getName());
    }

    @Test
    void deleteGiftCertificateCorrectly() throws IdNotFound {
        assertEquals("AWS Certificate", controller.delete(0).getName());
    }

    @Test
    void deleteGiftCertificateIDNotFound() {
        assertThrows(IdNotFound.class, () -> controller.delete(Integer.parseInt("1")).getName());
    }

}