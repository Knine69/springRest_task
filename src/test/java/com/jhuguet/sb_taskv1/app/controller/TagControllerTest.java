package com.jhuguet.sb_taskv1.app.controller;

import com.jhuguet.sb_taskv1.app.exceptions.CertificateAssociatedException;
import com.jhuguet.sb_taskv1.app.exceptions.IdNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.InvalidInputInformation;
import com.jhuguet.sb_taskv1.app.exceptions.MissingEntity;
import com.jhuguet.sb_taskv1.app.models.Tag;
import com.jhuguet.sb_taskv1.app.repositories.TagRepository;
import com.jhuguet.sb_taskv1.app.services.impl.TagServiceImpl;
import com.jhuguet.sb_taskv1.app.services.utils.SetUpUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    }


    @Test
    void get() throws IdNotFound, InvalidInputInformation {
        assertEquals(utils.sampleTag().getName(), controller.get("0").get(0).getName());
    }

    @Test
    void getAll() throws IdNotFound {
        assertEquals(utils.sampleTags().size(), controller.get(null).size());
    }

    @Test
    void getIDNotFound() {
        assertThrows(IdNotFound.class, () -> controller.get("1").get(0).getName());
    }

    @Test
    void save() throws MissingEntity {
        assertEquals(utils.sampleTag().getName(), controller.save(utils.sampleTag()).getName());
    }

    @Test
    void update() throws IdNotFound, InvalidInputInformation {
        assertEquals("newTag", controller.update(new Tag("newTag")).getName());
    }

    @Test
    void updateIDNotFound() {
        when(repository.existsById(anyInt())).thenReturn(false);
        assertThrows(IdNotFound.class, () -> controller.update(new Tag("newTag")).getName());
        when(repository.existsById(anyInt())).thenReturn(true);
    }

    @Test
    void updateInvalidInputInformation() {
        Tag tag = new Tag(-1, "newTag", new HashSet<>(utils.sampleCertificates()));
        assertThrows(InvalidInputInformation.class, () ->
                controller.update(tag).getName());
    }

    @Test
    void deleteCorrectly() throws IdNotFound, InvalidInputInformation, CertificateAssociatedException {
        when(repository.findById(0))
                .thenReturn(Optional.ofNullable(new Tag(0, "Cloud", new HashSet<>())));
        assertEquals(utils.sampleTag().getName(), controller.delete("0").getName());
        when(repository.findById(0))
                .thenReturn(Optional.ofNullable(utils.sampleTag()));
    }

    @Test
    void deleteAssociatedCertificate() {
        assertThrows(CertificateAssociatedException.class, () -> controller.delete("0").getName());
    }

    @Test
    void deleteIDNotFound() {
        assertThrows(IdNotFound.class, () -> controller.delete("1").getName());
    }

}