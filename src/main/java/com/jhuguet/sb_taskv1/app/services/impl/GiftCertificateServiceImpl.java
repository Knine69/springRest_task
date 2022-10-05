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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateRepository giftRepository;
    private final TagRepository tagRepository;
    private final Logger logger = Logger.getLogger(GiftCertificateServiceImpl.class.getName());

    private List<GiftCertificate> certificates;

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
        return giftRepository.findById(id).orElseThrow(IdNotFound::new);
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

    @Override
    public GiftCertificate updateTags(int certId, Set<Tag> tags) throws IdNotFound, InvalidIdInputInformation {
        GiftCertificate certificate = get(certId);
        certificate.cleanTags();
        tags.forEach(tag -> {
            if (tag.getId() != 0) {
                boolean isRepeated =
                        certificate.getAssociatedTags().stream()
                                .anyMatch(c -> c.getName().equalsIgnoreCase(tag.getName()));
                boolean isTagIDInRepository = tagRepository.findAll().stream()
                        .anyMatch(x -> x.getId() == tag.getId());

                boolean isTagNameInRepository = tagRepository.findAll().stream()
                        .anyMatch(x -> x.getName().equalsIgnoreCase(tag.getName()));

                if (!isRepeated) {
                    if ((isTagIDInRepository && isTagNameInRepository) ||
                            (!isTagIDInRepository && !isTagNameInRepository)
                    ) {
                        certificate.assignTag(tag);
                    } else {
                        logger.warning("Please input Tag information correctly, information conflict: "
                                + tag.getName());
                    }
                }

                if (!isTagIDInRepository && !isTagNameInRepository) {
                    tagRepository.save(tag);
                    logger.info("Adding non-existing tag into database.");
                }
            } else {
                logger.warning("Please input Tag with it's ID: " + tag.getName());
            }
        });
        giftRepository.save(certificate);
        return certificate;
    }

    @Override
    public List<GiftCertificate> getByTagName(String name) {
        certificates = new ArrayList<>();
        getAll().forEach(certificate -> certificate.getAssociatedTags().forEach(tag -> {
            if (tag.getName().equals(name)) {
                certificates.add(certificate);
            }
        }));

        return certificates;
    }

    @Override
    public List<GiftCertificate> getByPart(String part) {
        certificates = new ArrayList<>();
        getAll().forEach(certificate -> {
            if (certificate.getName().contains(part) || certificate.getDescription().contains(part)) {
                certificates.add(certificate);
            }
        });

        return certificates;
    }

    @Override
    public List<GiftCertificate> getByDateOrName(String sortBy, String order) {
        certificates = new ArrayList<>();
        if (validateTypeAndOrder(sortBy, order)) {
            certificates = sortCertificates(order.equalsIgnoreCase("asc"), order);
        } else {
            logger.info("Please enter an appropriate sorting and/or order types.");
        }

        return certificates;
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

    private List<GiftCertificate> sortCertificates(boolean ascending, String field) {
        List<GiftCertificate> result = getAll()
                .stream().sorted((certificate1, certificate2) -> field.equalsIgnoreCase("name") ?
                        certificate1.getName().compareTo(certificate2.getName()) :
                        certificate1.getCreateDate().compareTo(certificate2.getCreateDate()))
                .collect(Collectors.toList());

        if (ascending) {
            return result;
        }

        Collections.reverse(result);
        return result;
    }

    private boolean validateTypeAndOrder(String sortBy, String order) {
        return (sortBy.equalsIgnoreCase("name") || sortBy.equalsIgnoreCase("date")) &&
                (order.equalsIgnoreCase("asc") || order.equalsIgnoreCase("desc"));
    }
}
