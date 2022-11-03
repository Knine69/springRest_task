package com.jhuguet.sb_taskv1.app.services.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jhuguet.sb_taskv1.app.exceptions.IdAlreadyInUse;
import com.jhuguet.sb_taskv1.app.exceptions.IdNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.InvalidInputInformation;
import com.jhuguet.sb_taskv1.app.exceptions.MissingEntity;
import com.jhuguet.sb_taskv1.app.exceptions.PageNotFound;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
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


    public Page<GiftCertificate> getAllPageable(Pageable pageable) throws PageNotFound {
        Page<GiftCertificate> page = giftRepository.findAll(pageable);

        if (page.getTotalPages() <= pageable.getPageNumber()) {
            throw new PageNotFound();
        }
        return page;
    }


    public GiftCertificate get(int id) throws IdNotFound {
        return giftRepository.findById(id).orElseThrow(IdNotFound::new);
    }

    @Transactional
    public GiftCertificate save(GiftCertificate giftCertificate) throws MissingEntity, InvalidInputInformation, IdAlreadyInUse {
        if (!Objects.isNull(giftCertificate)) {
            savingValidations(giftCertificate);
            giftRepository.save(giftCertificate);
        } else {
            throw new MissingEntity();
        }

        return giftCertificate;
    }

    private void savingValidations(GiftCertificate certificate) throws InvalidInputInformation, IdAlreadyInUse {
        certificateValidations(certificate);
        tagValidations(certificate.getAssociatedTags());
    }

    private void certificateValidations(GiftCertificate certificate) throws InvalidInputInformation {
        String localDate = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        validateNegative(certificate.getDuration());
        validateNegative((certificate.getPrice().intValue()));

        certificate.setCreateDate(localDate);
        certificate.setLastUpdateDate(localDate);
    }

    private void tagValidations(Set<Tag> tags) throws InvalidInputInformation, IdAlreadyInUse {
        for (Tag t : tags) {
            validateNegative(t.getId());
            Tag tag = tagRepository.findById(t.getId()).get();
            if (!t.getName().equalsIgnoreCase(tag.getName())) {
                throw new IdAlreadyInUse();
            }
        }
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
    public Order placeNewOrder(List<Integer> certificatesIds, int userId) throws IdNotFound {
        User user = userRepository.findById(userId).orElseThrow(IdNotFound::new);
        Order order = new Order(orderRepository.findAll().size() + 1);

        for (Integer id : certificatesIds) {
            GiftCertificate certificate = get(id);
            placeOrderProcess(user, order, certificate);
        }

        userRepository.save(user);

        return order;
    }

    private void placeOrderProcess(User user, Order order, GiftCertificate certificate) {
        order.setUser(user);
        order.addCertificate(certificate);
        user.placeOrder(order);
    }

    @Override
    public Page<GiftCertificate> filterCertificates(
            String tagName,
            String nameOrDescriptionPart,
            String nameOrDate,
            String order,
            Pageable pageable) throws PageNotFound {

        List<GiftCertificate> resultList = getAllPageable(pageable).getContent();
        if (!tagName.isEmpty()) {
            resultList = getByTagName(resultList, tagName);
        }
        if (!nameOrDescriptionPart.isEmpty()) {
            resultList = getByPart(resultList, nameOrDescriptionPart);
        }
        if (!nameOrDate.isEmpty()) {
            resultList = getByDateOrName(resultList, nameOrDate, order);
        }

        return new PageImpl<>(resultList);
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

        return certificate;
    }

    private GiftCertificate partialUpdate(int id, Map<String, Object> patch) throws IdNotFound, InvalidInputInformation {
        GiftCertificate certificate = get(id);

        for (Map.Entry<String, Object> entry : patch.entrySet()) {
            switch (entry.getKey()) {
                case "name":
                    certificate.setName((String) entry.getValue());
                    break;
                case "description":
                    certificate.setDescription((String) entry.getValue());
                    break;
                case "price":
                    validateNegative((Double) entry.getValue());
                    certificate.setPrice(BigDecimal.valueOf((Double) entry.getValue()));
                    break;
                case "duration":
                    validateNegative((int) entry.getValue());
                    certificate.setDuration((int) entry.getValue());
                    break;
                case "associatedTags":
                    updateTags(certificate, convertToSet(entry.getValue()));
                    break;
                default:
                    logger.info("Field " + entry.getKey() + " is a non-updatable.");
                    break;
            }
        }
        certificate.setLastUpdateDate(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return certificate;
    }

    private void validateNegative(double value) throws InvalidInputInformation {
        if (value < 0) {
            throw new InvalidInputInformation();
        }
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
                predicate = certificate -> certificate.getName().contains(filterField)
                        || certificate.getDescription().contains(filterField);
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
