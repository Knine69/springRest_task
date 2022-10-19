package com.jhuguet.sb_taskv1.app.services.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jhuguet.sb_taskv1.app.exceptions.IdNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.InvalidInputInformation;
import com.jhuguet.sb_taskv1.app.exceptions.MissingEntity;
import com.jhuguet.sb_taskv1.app.models.GiftCertificate;
import com.jhuguet.sb_taskv1.app.models.Order;
import com.jhuguet.sb_taskv1.app.models.Tag;
import com.jhuguet.sb_taskv1.app.models.User;
import com.jhuguet.sb_taskv1.app.repositories.GiftCertificateRepository;
import com.jhuguet.sb_taskv1.app.repositories.OrderRepository;
import com.jhuguet.sb_taskv1.app.repositories.TagRepository;
import com.jhuguet.sb_taskv1.app.repositories.UserRepository;
import com.jhuguet.sb_taskv1.app.services.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateRepository giftRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final Logger logger = Logger.getLogger(GiftCertificateServiceImpl.class.getName());

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateRepository giftRepository, TagRepository tagRepository,
                                      UserRepository userRepository, OrderRepository orderRepository) {
        this.giftRepository = giftRepository;
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }


    public List<GiftCertificate> getAll() {
        return giftRepository.findAll();
    }

    public GiftCertificate get(int id) throws IdNotFound {
        return giftRepository.findById(id).orElseThrow(IdNotFound::new);
    }

    @Transactional
    public GiftCertificate save(GiftCertificate giftCertificate) throws MissingEntity {
        if (!Objects.isNull(giftCertificate)) {
            String localDate = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            giftCertificate.setCreateDate(localDate);
            giftCertificate.setLastUpdateDate(localDate);
            giftRepository.save(giftCertificate);
        } else {
            throw new MissingEntity();
        }

        return giftCertificate;
    }

    @Override
    public GiftCertificate update(int id,
                                  Map<String, Object> patch) throws IdNotFound, InvalidInputInformation {
        validateEntity(id, "certificate");
        GiftCertificate certificate = partialUpdate(id, patch);
        giftRepository.save(certificate);

        return certificate;
    }

    @Override
    public GiftCertificate placeNewInOrder(int certID, int orderID, int userID) throws IdNotFound {
        GiftCertificate dbCertificate = get(certID);
        GiftCertificate copyCertificate = copyCertificate(dbCertificate);

        User user = userRepository.findById(userID).orElseThrow(IdNotFound::new);
        Order order = orderRepository.findById(orderID).orElseThrow(IdNotFound::new);

        if (user.getOrders().stream().anyMatch(o -> o.getId() == orderID)) {
            if (!order.getCertificates().stream().anyMatch(c -> c.getId() == certID)) {
                placeOrderProcess(user, order, copyCertificate);
            } else {
                logger.warning("Certificate already in order");
            }
        }

        userRepository.save(user);

        return copyCertificate;
    }

    @Override
    public GiftCertificate placeNewOrder(int certID, int userID) throws IdNotFound {
        GiftCertificate certificate = get(certID);
        User user = userRepository.findById(userID).orElseThrow(IdNotFound::new);
        Order order = new Order(new Date());

        order.setUser(user);
        placeOrderProcess(user, order, certificate);
        userRepository.save(user);

        return certificate;
    }

    private void placeOrderProcess(User user, Order order, GiftCertificate certificate) {
        order.addCertificate(certificate);
        user.placeOrder(order);
    }

    private GiftCertificate copyCertificate(GiftCertificate certificate) {
        return new GiftCertificate(
                certificate.getId(),
                certificate.getName(),
                certificate.getDescription(),
                certificate.getPrice(),
                certificate.getDuration(),
                certificate.getLastUpdateDate(),
                certificate.getCreateDate(),
                certificate.getAssociatedTags()
        );
    }

    @Override
    public List<GiftCertificate> filterCertificates(String tagName, String nameOrDescriptionPart, String nameOrDate, String order) {
        List<GiftCertificate> resultList = getAll();
        if (!tagName.isEmpty()) {
            resultList = getByTagName(resultList, tagName);
        }
        if (!nameOrDescriptionPart.isEmpty()) {
            resultList = getByPart(resultList, nameOrDescriptionPart);
        }
        if (!nameOrDate.isEmpty()) {
            resultList = getByDateOrName(resultList, nameOrDate, order);
        }

        return resultList;
    }

    private List<GiftCertificate> getByTagName(List<GiftCertificate> currentList, String name) {
        return filterCertificates(currentList, "tagName", name);
    }

    private List<GiftCertificate> getByPart(List<GiftCertificate> currentList, String part) {
        return filterCertificates(currentList, "nameDescriptionPart", part);
    }

    private List<GiftCertificate> getByDateOrName(List<GiftCertificate> currentList, String sortBy, String order) {
        List<GiftCertificate> certificateList = new ArrayList<>();
        if (validateNameAndDateTypes(sortBy, order)) {
            certificateList = filterCertificates(currentList, sortBy, "");
            return sortCertificates(certificateList, sortBy, order);
        } else {
            logger.info("Please enter an appropriate sorting and/or order types.");
        }

        return certificateList;
    }

    @Transactional
    public GiftCertificate delete(int id) throws IdNotFound {
        GiftCertificate certificate = get(id);
        giftRepository.deleteById(id);
        logger.info("Deleted GifCertificate from database: " + new Gson().toJson(certificate));

        return certificate;
    }

    private GiftCertificate partialUpdate(int id, Map<String, Object> patch) throws IdNotFound {
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
                    if (validateNegativeDouble((double) value)) {
                        certificate.setPrice(BigDecimal.valueOf((Double) value));
                    }
                    break;
                case "duration":
                    if (validateNegativeInteger((int) value)) {
                        certificate.setDuration((int) value);
                    }
                    break;
                case "associatedTags":
                    updateTags(certificate, convertToSet(value));
                    break;
                default:
                    logger.info("Field " + key + " is a non-updatable.");
                    break;
            }
            certificate.setLastUpdateDate(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        });

        return certificate;
    }

    private boolean validateNegativeInteger(int value) {
        if (value < 0) {
            logger.warning("Duration incorrect information to update. Won't be updated.");
            return false;
        }
        return true;
    }

    private boolean validateNegativeDouble(double value) {
        if (value < 0) {
            logger.warning("Price incorrect information to update. Won't be updated.");
            return false;
        }
        return true;
    }

    private boolean validateNameAndDateTypes(String sortBy, String order) {
        boolean orderValid = (order.equalsIgnoreCase("asc") || order.equalsIgnoreCase("desc"));
        boolean inputValid = false;

        switch (sortBy) {
            case "name":
            case "createDate":
            case "lastUpdateDate":
                inputValid = true;
                break;
            default:
                break;
        }

        return orderValid && inputValid;
    }

    private List<GiftCertificate> sortCertificates(List<GiftCertificate> certificatesList, String sortBy, String order) {
        Comparator<GiftCertificate> comparator = null;
        switch (sortBy) {
            case "name":
                comparator = Comparator.comparing(GiftCertificate::getName);
                break;
            case "duration":
                comparator = Comparator.comparing(GiftCertificate::getDuration);
                break;
            case "description":
                comparator = Comparator.comparing(GiftCertificate::getDescription);
                break;
            case "price":
                comparator = Comparator.comparing(GiftCertificate::getPrice);
                break;
            case "createDate":
                comparator = Comparator.comparing(GiftCertificate::getCreateDate);
                break;
            case "lastUpdateDate":
                comparator = Comparator.comparing(GiftCertificate::getLastUpdateDate);
                break;
            default:
                logger.warning("Please input correct sort type.");
                break;
        }

        return order.equalsIgnoreCase("asc") || order.equals("") ?
                certificatesList.stream().sorted(comparator).collect(Collectors.toList()) :
                certificatesList.stream().sorted(comparator.reversed()).collect(Collectors.toList());
    }

    private List<GiftCertificate> filterCertificates(List<GiftCertificate> currentList, String parameter, String filterField) {
        Predicate<GiftCertificate> predicate = null;
        switch (parameter) {
            case "nameDescriptionPart":
                predicate = certificate -> certificate.getName().contains(filterField) || certificate.getDescription().contains(filterField);
                break;
            case "createDate":
                predicate = certificate -> certificate.getCreateDate().contains(filterField);
                break;
            case "lastUpdateDate":
                predicate = certificate -> certificate.getLastUpdateDate().contains(filterField);
                break;
            case "name":
                predicate = certificate -> certificate.getName().contains(filterField);
                break;
            case "tagName":
                predicate = certificate -> certificate.getAssociatedTags().stream()
                        .anyMatch(tag -> tag.getName().equalsIgnoreCase(filterField));
            default:
                logger.warning("Please input variant correctly.");
                break;
        }
        return predicate != null ? currentList.stream().filter(predicate).collect(Collectors.toList()) : new ArrayList<>();
    }

    private void validateEntity(int id, String option) throws InvalidInputInformation, IdNotFound {
        if (id < 0) {
            throw new InvalidInputInformation();
        }

        switch (option) {
            case "order":
                if (!orderRepository.existsById(id)) {
                    throw new IdNotFound();
                }
                break;
            case "certificate":
                if (!giftRepository.existsById(id)) {
                    throw new IdNotFound();
                }
                break;
            case "user":
                if (!userRepository.existsById(id)) {
                    throw new IdNotFound();
                }
                break;
            default:
                logger.warning("Please enter a valid entity to verify");
                break;
        }
    }

    private Set<Tag> convertToSet(Object tags) {
        return new Gson().fromJson(tags.toString(), new TypeToken<HashSet<Tag>>() {
        }.getType());
    }

    private boolean validateTagPassed(Tag tag) {
        boolean isTagIDInRepository = tagRepository.findAll().stream()
                .anyMatch(x -> x.getId() == tag.getId());
        boolean isTagNameInRepository = tagRepository.findAll().stream()
                .anyMatch(x -> x.getName().equalsIgnoreCase(tag.getName()));

        return (isTagIDInRepository && isTagNameInRepository) ||
                (!isTagIDInRepository && !isTagNameInRepository);
    }

    private void updateTags(GiftCertificate certificate, Set<Tag> tags) {
        certificate.cleanTags();
        tags.forEach(tag -> {
            if (tag.getId() != 0) {
                boolean isRepeated =
                        certificate.getAssociatedTags().stream()
                                .anyMatch(c -> c.getName().equalsIgnoreCase(tag.getName()));
                if (!isRepeated) {
                    if (validateTagPassed(tag)
                    ) {
                        certificate.assignTag(tag);
                    } else {
                        logger.warning("Please input Tag information correctly, information conflict: "
                                + tag.getName());
                    }
                }
            } else {
                logger.warning("Please input Tag with it's ID: " + tag.getName());
            }
        });
    }
}
