package com.jhuguet.sb_taskv1.app.services.impl;

import com.jhuguet.sb_taskv1.app.exceptions.IdAlreadyInUse;
import com.jhuguet.sb_taskv1.app.exceptions.IdNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.InvalidInputInformation;
import com.jhuguet.sb_taskv1.app.exceptions.MissingEntity;
import com.jhuguet.sb_taskv1.app.exceptions.PageNotFound;
import com.jhuguet.sb_taskv1.app.models.GiftCertificate;
import com.jhuguet.sb_taskv1.app.repositories.GiftCertificateRepository;
import com.jhuguet.sb_taskv1.app.repositories.OrderRepository;
import com.jhuguet.sb_taskv1.app.repositories.TagRepository;
import com.jhuguet.sb_taskv1.app.repositories.UserRepository;
import com.jhuguet.sb_taskv1.app.services.utils.SetUpUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
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
import static org.mockito.Mockito.when;

@SpringBootTest(classes = GiftCertificateServiceImplTest.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GiftCertificateServiceImplTest {

    private static final SetUpUtils utils = new SetUpUtils();
    static final GiftCertificateRepository giftCertificateRepository = Mockito.mock(GiftCertificateRepository.class);
    static final TagRepository tagRepository = Mockito.mock(TagRepository.class);
    static final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private static final OrderRepository orderRepository = Mockito.mock(OrderRepository.class);
    GiftCertificateServiceImpl giftCertificateService =
            new GiftCertificateServiceImpl(giftCertificateRepository, tagRepository, userRepository, orderRepository);

    @BeforeAll
    private void setMocks() {
        when(giftCertificateRepository.existsById(anyInt())).thenReturn(true);
        when(giftCertificateRepository.findById(0))
                .thenReturn(Optional.ofNullable(utils.sampleCertificate()));
        when(giftCertificateRepository.findAll())
                .thenReturn(utils.sampleCertificates());
        when(giftCertificateRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(
                        new ArrayList<>(utils.sampleCertificates()), PageRequest.of(1, 1), utils.sampleTags().size()
                ));

        when(tagRepository.existsById(anyInt())).thenReturn(true);
        when(tagRepository.findById(0))
                .thenReturn(Optional.ofNullable(utils.sampleTag()));
        when(tagRepository.findAll())
                .thenReturn(new ArrayList<>(utils.sampleTags()));

        when(tagRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(
                        new ArrayList<>(utils.sampleTags()), PageRequest.of(1, 1), utils.sampleTags().size()
                ));

    }


    @Test
    void checkAllCertificatesFromRepositoryAreReturnedSuccessfully() throws PageNotFound {
        assertEquals(utils.sampleCertificates().size(),
                giftCertificateService.getAllPageable(PageRequest.of(1, 1)).getContent().size());
    }

    @Test
    void getGiftCertificateIdExists() throws IdNotFound {
        GiftCertificate certificate = utils.sampleCertificate();
        assertEquals(certificate.getName(), giftCertificateService.get(anyInt()).getName());
    }

    @Test
    void getGiftCertificateErrorIfIdNotFound() {
        assertThrows(IdNotFound.class, () -> giftCertificateService.get(-1));
    }

    @Test
    void filterCertificatesByTagNameIfSpecified() throws PageNotFound {
        assertEquals(utils.sampleCertificates().size(),
                giftCertificateService.filterCertificates(
                        "Cloud", "", "", "",
                        PageRequest.of(1, 1)).getTotalElements());
    }

    @Test
    void filterCertificatesByPartOfNameOrDescriptionIfSpecified() throws PageNotFound {
        assertEquals(utils.sampleCertificates().size(),
                giftCertificateService.filterCertificates("", "Master", "", "",
                        PageRequest.of(1, 1)).getTotalElements());
    }

    @Test
    void filterCertificatesByCreateDateIfSpecified() throws PageNotFound {
        assertEquals(utils.sampleCertificates().size(),
                giftCertificateService.filterCertificates("", "", "createDate", "asc",
                        PageRequest.of(1, 1)).getTotalElements());
    }

    @Test
    void filterCertificatesByLastUpdateDateIfSpecified() throws PageNotFound {
        assertEquals(utils.sampleCertificates().size(),
                giftCertificateService.filterCertificates("", "", "lastUpdateDate", "asc",
                        PageRequest.of(1, 1)).getTotalElements());
    }

    @Test
    void filterCertificatesByName() throws PageNotFound {
        assertEquals(utils.sampleCertificates().size(),
                giftCertificateService.filterCertificates("", "", "name", "asc",
                        PageRequest.of(1, 1)).getTotalElements());
    }

    @Test
    void saveGiftCertificateIfCertificateIsValid() throws MissingEntity, InvalidInputInformation, IdAlreadyInUse {
        GiftCertificate certificate = utils.sampleCertificate();
        assertEquals(giftCertificateService.save(certificate), certificate);
    }

    @Test
    void saveGiftCertificateExceptionIfCertificateIsNull() {
        assertThrows(MissingEntity.class, () -> giftCertificateService.save(null));
    }

    @Test
    void updateGiftCertificateExceptionIfIdNotFound() {
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
    void updateGiftCertificateExceptionIfIdLessOrEqualToZero() {
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
    void deleteGiftCertificateCorrectlyIfGivenIdExists() throws IdNotFound {
        assertEquals(giftCertificateService.delete(anyInt()).getName(), utils.sampleCertificate().getName());
    }
}