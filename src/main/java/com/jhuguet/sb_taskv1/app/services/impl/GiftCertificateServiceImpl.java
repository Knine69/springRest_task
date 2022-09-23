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
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateRepository giftRepository;
    private final TagRepository tagRepository;
    private final Logger logger = Logger.getLogger(GiftCertificateServiceImpl.class.getName());

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
    public GiftCertificate updateTags(int certId, List<Tag> tags) throws IdNotFound, InvalidIdInputInformation {
        GiftCertificate certificate = get(certId);
        certificate.cleanTags();
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

    @Override
    public List<GiftCertificate> getByTagName(String name) {
        List<GiftCertificate> certificates = new ArrayList<>();

        getAll().forEach(certificate -> certificate.getAssociatedTags().forEach(tag -> {
            if (tag.getName().equals(name)) {
                certificates.add(certificate);
            }
        }));

        return certificates;
    }

    @Override
    public List<GiftCertificate> getByPart(String part) {
        List<GiftCertificate> certificates = new ArrayList<>();

        getAll().forEach(certificate -> {
            if (certificate.getName().contains(part) || certificate.getDescription().contains(part)) {
                certificates.add(certificate);
            }
        });

        return certificates;
    }

    @Override
    public List<GiftCertificate> getByDateOrName(String sortBy, String order) {
        List<GiftCertificate> certificates = new ArrayList<>();
        boolean ascendingOrder = order.equals("asc");
        switch (sortBy) {
            case "name":
                certificates = sortCertificatesByName(ascendingOrder);
                break;
            case "date":
                certificates = sortCertificatesByDate(ascendingOrder);
                break;
            default:
                logger.info("Please enter an appropriate sorting type.");
                break;
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

    private List<GiftCertificate> sortCertificatesByName(boolean ascending) {
        return ascending ?
                getAll()
                        .stream().sorted(Comparator.comparing(GiftCertificate::getName))
                        .collect(Collectors.toList()) :
                getAll()
                        .stream().sorted(Comparator.comparing(GiftCertificate::getName).reversed())
                        .collect(Collectors.toList());

    }

    private List<GiftCertificate> sortCertificatesByDate(boolean ascending) {
        return ascending ?
                getAll()
                        .stream().sorted(Comparator.comparing(GiftCertificate::getCreateDate))
                        .collect(Collectors.toList()) :
                getAll()
                        .stream().sorted(Comparator.comparing(GiftCertificate::getCreateDate).reversed())
                        .collect(Collectors.toList());

    }
}
