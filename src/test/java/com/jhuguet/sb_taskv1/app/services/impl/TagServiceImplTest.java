package com.jhuguet.sb_taskv1.app.services.impl;

import com.jhuguet.sb_taskv1.app.exceptions.CertificateAssociatedException;
import com.jhuguet.sb_taskv1.app.exceptions.IdNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.InvalidIdInputInformation;
import com.jhuguet.sb_taskv1.app.models.Tag;
import com.jhuguet.sb_taskv1.app.repositories.TagRepository;
import com.jhuguet.sb_taskv1.app.services.utils.SetUpUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class TagServiceImplTest {

    private static final SetUpUtils utils = new SetUpUtils();
    static final TagRepository tagRepository = Mockito.mock(TagRepository.class);
    TagServiceImpl service = new TagServiceImpl(tagRepository);

    @BeforeAll
    private static void setMocks() {
        //Setting up mocks for tagRepository
        when(tagRepository.existsById(anyInt())).thenReturn(true);
        when(tagRepository.findById(anyInt()))
                .thenReturn(Optional.ofNullable(utils.sampleTag()));
        when(tagRepository.findAll())
                .thenReturn(utils.sampleTags());

    }

    @Test
    void getAll() {
        assertEquals(service.getAll().size(), utils.sampleTags().size());
    }

    @Test
    void get() throws IdNotFound, InvalidIdInputInformation {
        assertEquals(service.get(anyInt()).getName(), utils.sampleTag().getName());
        assertThrows(InvalidIdInputInformation.class, () ->
                service.get(-1)
        );
        assertThrows(IdNotFound.class, () -> {
            when(tagRepository.existsById(anyInt())).thenReturn(false);
            service.get(1);
        });
        when(tagRepository.existsById(anyInt())).thenReturn(true);

    }

    @Test
    void save() {
        Tag sample = utils.sampleTag();
        assertEquals(service.save(sample).getName(), utils.sampleTag().getName());
        assertThrows(NullPointerException.class, () -> service.save(null).getName());
    }

    @Test
    void update() throws IdNotFound, InvalidIdInputInformation {
        Tag tag = utils.sampleTag();
        tag.setName("TestCertificate");
        assertEquals(service.update(tag).getName(), "TestCertificate");
        assertNotEquals(service.update(tag).getName(), utils.sampleTag().getName());
    }

    @Test
    void delete() throws IdNotFound, InvalidIdInputInformation, CertificateAssociatedException {
        assertThrows(CertificateAssociatedException.class, () -> service.delete(anyInt()));
        when(tagRepository.findById(anyInt()))
                .thenReturn(Optional.of(new Tag("Cloud", new HashSet<>())));
        assertEquals(service.delete(anyInt()).getName(), utils.sampleTag().getName());
    }
}