package com.jhuguet.sb_taskv1.app.services.impl;

import com.jhuguet.sb_taskv1.app.exceptions.CertificateAssociatedException;
import com.jhuguet.sb_taskv1.app.exceptions.IdNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.InvalidInputInformation;
import com.jhuguet.sb_taskv1.app.exceptions.MissingEntity;
import com.jhuguet.sb_taskv1.app.exceptions.PageNotFound;
import com.jhuguet.sb_taskv1.app.models.Tag;
import com.jhuguet.sb_taskv1.app.repositories.TagRepository;
import com.jhuguet.sb_taskv1.app.services.utils.SetUpUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TagServiceImplTest {

    private final SetUpUtils utils = new SetUpUtils();
    static final TagRepository tagRepository = Mockito.mock(TagRepository.class);
    TagServiceImpl service = new TagServiceImpl(tagRepository);

    @BeforeAll
    private void setMocks() {
        when(tagRepository.existsById(anyInt())).thenReturn(true);
        when(tagRepository.findById(anyInt()))
                .thenReturn(Optional.ofNullable(utils.sampleTag()));
        when(tagRepository.findAll())
                .thenReturn(new ArrayList<>(utils.sampleTags()));

        when(tagRepository.findAll(any(Pageable.class)))
                .thenReturn(utils.samplePageTags());

    }

    @Test
    void getAllTagsInRepositoryIfCorrectlyGivenPagingParams() throws PageNotFound {
        assertEquals(utils.sampleTags().size(), service.getAll(PageRequest.of(1, 1)).getTotalElements());
    }

    @Test
    void getAllTagsInRepositoryIfIncorrectlyGivenPagingParams() {
        assertThrows(PageNotFound.class, () -> service.getAll(PageRequest.of(10, 1)).getTotalElements());
    }

    @Test
    void getTagIfGivenIdIfExists() throws IdNotFound {
        assertEquals(service.get(anyInt()).getName(), utils.sampleTag().getName());
    }

    @Test
    void saveTagIfGivenTagObjectIsValid() throws MissingEntity, InvalidInputInformation {
        assertEquals(service.save(utils.sampleTag()).getName(), utils.sampleTag().getName());
    }

    @Test
    void saveTagExceptionIfGivenTagObjectIsNotValid() {
        assertThrows(InvalidInputInformation.class, () -> service.save(new Tag(-1, "Science")));
    }

    @Test
    void saveTagExceptionIfGivenTagObjectIsNull() {
        assertThrows(MissingEntity.class, () -> service.save(null));
    }

    @Test
    void deleteTagCorrectlyIfNoCertificatesAreAssociated() throws IdNotFound, CertificateAssociatedException {
        when(tagRepository.findById(anyInt()))
                .thenReturn(Optional.of(new Tag("Cloud", new HashSet<>())));
        assertEquals(service.delete(anyInt()).getName(), utils.sampleTag().getName());
    }

    @Test
    void deleteTagExceptionIfCertificatesAreAssociated() {
        when(tagRepository.findById(anyInt()))
                .thenReturn(Optional.of(utils.sampleTag()));
        assertThrows(CertificateAssociatedException.class, () -> service.delete(anyInt()));
    }
}