package com.jhuguet.sb_taskv1.app.services.impl;

import com.jhuguet.sb_taskv1.app.exceptions.IdNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.InvalidIdInputInformation;
import com.jhuguet.sb_taskv1.app.models.GiftCertificate;
import com.jhuguet.sb_taskv1.app.models.Tag;
import com.jhuguet.sb_taskv1.app.repositories.GiftCertificateRepository;
import com.jhuguet.sb_taskv1.app.repositories.TagRepository;
import com.jhuguet.sb_taskv1.app.services.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private GiftCertificateRepository giftRepository;
    private TagRepository tagRepository;
    private Logger logger = Logger.getLogger(GiftCertificateServiceImpl.class.getName());

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateRepository giftRepository, TagRepository tagRepository) {
        this.giftRepository = giftRepository;
        this.tagRepository = tagRepository;
    }

    public List<GiftCertificate> getAll() {
        return giftRepository.findAll();
    }

    public GiftCertificate get(int id) throws IdNotFound, InvalidIdInputInformation {
        validateCertificate(id);
        return giftRepository.findById(id).get();
    }

    @Transactional
    public GiftCertificate save(GiftCertificate giftCertificate) {
        String localDate = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        giftCertificate.setCreateDate(localDate);
        giftCertificate.setLastUpdateDate(localDate);
        giftRepository.save(giftCertificate);

        return giftCertificate;
    }

    @Override
    public GiftCertificate update(int id,
                                  Map<String, Object> patch) throws IdNotFound, InvalidIdInputInformation {

        GiftCertificate certificate = partialUpdate(id, patch);
        giftRepository.save(certificate);

        return certificate;
    }

    private GiftCertificate partialUpdate(int id, Map<String, Object> patch) throws IdNotFound, InvalidIdInputInformation {
        GiftCertificate certificate = get(id);
        patch.forEach((key, value) -> {
            switch (key) {
                case "name":
                    certificate.setName((String) value);
                    break;
                case "description":
                    certificate.setDescription((String) value);
                    break;
                case "price":
                    certificate.setPrice(BigDecimal.valueOf((Double) value));
                    break;
                case "duration":
                    certificate.setDuration((int) value);
                    break;
                case "lastUpdateDate":
                    certificate.setLastUpdateDate(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                    break;
                default:
                    logger.info("Field " + key + " is a non-updatable.");
                    break;
            }
        });

        return certificate;
    }

    //Validate deletion of tags and amount of tags added
    @Override
    public GiftCertificate addTag(int certId, List<Tag> tags) throws IdNotFound, InvalidIdInputInformation {
        GiftCertificate certificate = get(certId);
        tags.forEach(tag -> {
            boolean isRepeated =
                    certificate.getAssociatedTags().stream()
                            .anyMatch(c -> c.getName().equals(tag.getName()));

            if (!isRepeated) {
                certificate.assignTag(tag);
            }

            if (!tagRepository.existsById(tag.getId())) {
                tagRepository.save(tag);
                logger.info("Adding non-existing tag into database.");
            }

        });
        giftRepository.save(certificate);
        return certificate;
    }

    @Transactional
    public GiftCertificate delete(int id) throws IdNotFound, InvalidIdInputInformation {
        GiftCertificate certificate = get(id);
        giftRepository.deleteById(id);

        return certificate;
    }

    private void validateCertificate(int id) throws IdNotFound, InvalidIdInputInformation {
        if (id < 0) {
            throw new InvalidIdInputInformation();
        }

        if (!giftRepository.existsById(id)) {
            throw new IdNotFound();
        }
    }
}
