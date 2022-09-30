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

    /**
     * Returns all GiftCertificates found in Database
     *
     * @return List of GiftCertificates
     */
    public List<GiftCertificate> getAll() {
        return giftRepository.findAll();
    }

    /**
     * Returns a single GiftCertificate retrieved based on ID
     *
     * @param id Given ID to filter and return specific GiftCertificate
     * @return Will return pertaining GiftCertificate retrieved from DB
     * @throws IdNotFound                Exception thrown when given ID is not found
     * @throws InvalidIdInputInformation Exception thrown when given ID is incorrectly entered
     */
    public GiftCertificate get(int id) throws IdNotFound, InvalidIdInputInformation {
        validateCertificate(id);
        return giftRepository.findById(id).get();
    }

    /**
     * Will save a GiftCertificate into Database
     *
     * @param giftCertificate GiftCertificate Object which will be saved
     * @return GiftCertificate object saved into Database
     */
    @Transactional
    public GiftCertificate save(GiftCertificate giftCertificate) {
        String localDate = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        giftCertificate.setCreateDate(localDate);
        giftCertificate.setLastUpdateDate(localDate);
        giftRepository.save(giftCertificate);

        return giftCertificate;
    }

    /**
     * Will patch only specified changes into GiftCertificate excluding Tags
     *
     * @param id    ID of GiftCertificate to which will be applied the patch
     * @param patch Desired changes to apply
     * @return GiftCertificate after patched without Tags being patched to it
     * @throws IdNotFound                Exception thrown when given ID is not found
     * @throws InvalidIdInputInformation Exception thrown when given ID is incorrectly entered
     */
    @Override
    public GiftCertificate update(int id,
                                  Map<String, Object> patch) throws IdNotFound, InvalidIdInputInformation {

        GiftCertificate certificate = partialUpdate(id, patch);
        giftRepository.save(certificate);

        return certificate;
    }

    /**
     * Utility method used in order to update only required fields of GiftCertificate
     *
     * @param id    ID of GiftCertificate to which will be applied the patch
     * @param patch Desired changes to apply
     * @return GiftCertificate after patched without Tags being patched to it
     * @throws IdNotFound                Exception thrown when given ID is not found
     * @throws InvalidIdInputInformation Exception thrown when given ID is incorrectly entered
     */
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

    /**
     * Will patch new Set of Tags to be linked to a GiftCertificates
     *
     * @param certId Certificate ID to apply the patch for
     * @param tags   Tag list to be added to specific GiftCertificate
     * @return GiftCertificate after being patched
     * @throws IdNotFound                Exception thrown when given ID is not found
     * @throws InvalidIdInputInformation Exception thrown when given ID is incorrectly entered
     */
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

    /**
     * Filter function used to retrieve a List of GiftCertificates based on Tag name
     *
     * @param name Tag's name used to filter
     * @return List of GiftCertificates
     */
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

    /**
     * Filter function used to retrieve a List of GiftCertificates based on part of name or description
     *
     * @param part Part of name or description that will be used to filter
     * @return List of GiftCertificates
     */
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

    /**
     * Filter function used to retrieve a List of GiftCertificates based on name or date, in ascendant
     * or descendant order
     *
     * @param sortBy Parameter given to sort by, either Date or Name
     * @param order  Order in which is needed to sort list, ascendant or descendant
     * @return List of GiftCertificates
     */
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

    /**
     * Deleting function of a specific GiftCertificate
     *
     * @param id Specific ID of GiftCertificate to be deleted
     * @return GiftCertificate which was deleted
     * @throws IdNotFound                Exception thrown when given ID is not found
     * @throws InvalidIdInputInformation Exception thrown when given ID is incorrectly entered
     */
    @Transactional
    public GiftCertificate delete(int id) throws IdNotFound, InvalidIdInputInformation {
        GiftCertificate certificate = get(id);
        giftRepository.deleteById(id);

        return certificate;
    }

    /**
     * Util function used in order to validate if ID entered is of valid format and does exist in Database
     *
     * @param id Specific ID of GiftCertificate to be deleted
     * @throws IdNotFound                Exception thrown when given ID is not found
     * @throws InvalidIdInputInformation Exception thrown when given ID is incorrectly entered
     */
    private void validateCertificate(int id) throws IdNotFound, InvalidIdInputInformation {
        if (id < 0) {
            throw new InvalidIdInputInformation();
        }

        if (!giftRepository.existsById(id)) {
            throw new IdNotFound();
        }
    }

    /**
     * Util function used in order to sort by name List of GiftCertificates in ascendant or descendant order
     *
     * @param ascending Sorting order boolean value
     * @return List of GiftCertificates
     */
    private List<GiftCertificate> sortCertificatesByName(boolean ascending) {
        return ascending ?
                getAll()
                        .stream().sorted(Comparator.comparing(GiftCertificate::getName))
                        .collect(Collectors.toList()) :
                getAll()
                        .stream().sorted(Comparator.comparing(GiftCertificate::getName).reversed())
                        .collect(Collectors.toList());

    }

    /**
     * Util function used in order to sort by date List of GiftCertificates in ascendant or descendant order
     *
     * @param ascending Sorting order boolean value
     * @return List of GiftCertificates
     */
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
