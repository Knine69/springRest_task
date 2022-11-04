package com.jhuguet.sb_taskv1.app.controller;

import com.jhuguet.sb_taskv1.app.exceptions.CertificateAssociatedException;
import com.jhuguet.sb_taskv1.app.exceptions.IdNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.InvalidInputInformation;
import com.jhuguet.sb_taskv1.app.exceptions.MissingEntity;
import com.jhuguet.sb_taskv1.app.exceptions.PageNotFound;
import com.jhuguet.sb_taskv1.app.models.Tag;
import com.jhuguet.sb_taskv1.app.repositories.TagRepository;
import com.jhuguet.sb_taskv1.app.services.impl.TagServiceImpl;
import com.jhuguet.sb_taskv1.app.services.utils.SetUpUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class TagControllerTest {

    private static final SetUpUtils utils = new SetUpUtils();

    private static final TagRepository repository = Mockito.mock(TagRepository.class);

    private static final TagServiceImpl service = new TagServiceImpl(repository);

    private static final TagController controller = new TagController(service);

    @BeforeAll
    private static void mocksSetuUp() {
        when(repository.existsById(anyInt())).thenReturn(true);
        when(repository.findById(0))
                .thenReturn(Optional.ofNullable(utils.sampleTag()));
        when(repository.findAll())
                .thenReturn(new ArrayList<>(utils.sampleTags()));
        when(repository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(new ArrayList<>(utils.sampleTags())));
    }


    @Test
    void getTagIfCorrectlyGivenId() throws IdNotFound {
        assertEquals(utils.sampleTag().getName(), controller.get(0).getName());
    }

    @Test
    void getAllTagsIfCorrectlyGivenPagingParams() throws InvalidInputInformation, PageNotFound {
        assertEquals(utils.sampleTags().size(), controller.getAll(1, 1, "asc").getContent().getTotalElements());
    }

    @Test
    void getTagIncorrectlyIdNotFound() {
        assertThrows(IdNotFound.class, () -> controller.get(1).getName());
    }

    @Test
    void saveTagIfGivenComplyingTagObject() throws MissingEntity, InvalidInputInformation {
        assertEquals(utils.sampleTag().getName(), controller.save(utils.sampleTag()).getName());
    }

    @Test
    void deleteTagIfCorrectlyGivenId() throws IdNotFound, CertificateAssociatedException {
        when(repository.findById(0))
                .thenReturn(Optional.ofNullable(new Tag(0, "Cloud", new HashSet<>())));
        assertEquals(utils.sampleTag().getName(), controller.delete(0).getName());
        when(repository.findById(0))
                .thenReturn(Optional.ofNullable(utils.sampleTag()));
    }

    @Test
    void deleteTagExceptionAssociatedCertificateIfGiftCertificateIsAssociated() {
        assertThrows(CertificateAssociatedException.class, () -> controller.delete(0).getName());
    }

    @Test
    void deleteTagExceptionIfIdNotFound() {
        assertThrows(IdNotFound.class, () -> controller.delete(1).getName());
    }

}