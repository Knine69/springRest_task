package com.jhuguet.sb_taskv1.repositoryImp;

import com.jhuguet.sb_taskv1.models.Tag;
import com.jhuguet.sb_taskv1.repositories.TagRepository;
import com.jhuguet.sb_taskv1.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.transaction.TransactionalException;
import java.util.List;
import java.util.Optional;

@Service
public class TagRepositoryImpl implements TagService {

    private TagRepository tagRepository;

    public List<Tag> getAllTags() throws RuntimeException {
        return tagRepository.findAll();
    }

    public Optional<Tag> getTag(int tagId) throws RuntimeException {
        return tagRepository.findById(tagId);
    }

    @Transactional
    public void saveTag(Tag tagToSave) throws TransactionalException {
        tagRepository.save(tagToSave);
    }

    @Transactional
    public void deleteTag(int tagId) throws TransactionalException {
        tagRepository.deleteById(tagId);
    }
}
