package com.jhuguet.sb_taskv1.app.services.impl;

import com.jhuguet.sb_taskv1.app.exceptions.IdNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.NoExistingOrders;
import com.jhuguet.sb_taskv1.app.exceptions.NoTagInOrder;
import com.jhuguet.sb_taskv1.app.exceptions.OrderNotRelated;
import com.jhuguet.sb_taskv1.app.exceptions.PageNotFound;
import com.jhuguet.sb_taskv1.app.models.Order;
import com.jhuguet.sb_taskv1.app.models.Tag;
import com.jhuguet.sb_taskv1.app.models.User;
import com.jhuguet.sb_taskv1.app.repositories.OrderRepository;
import com.jhuguet.sb_taskv1.app.repositories.TagRepository;
import com.jhuguet.sb_taskv1.app.repositories.UserRepository;
import com.jhuguet.sb_taskv1.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, TagRepository tagRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public User get(int id) throws IdNotFound {
        return userRepository.findById(id).orElseThrow(IdNotFound::new);
    }

    @Override
    public Page<User> getAll(Pageable pageable) throws PageNotFound {

        Page<User> page = userRepository.findAll(pageable);

        if (page.getTotalPages() <= pageable.getPageNumber()) {
            throw new PageNotFound();
        }

        return page;
    }

    @Override
    public List<Order> getOrders(int id) throws IdNotFound {
        User user = get(id);
        return new ArrayList<>(user.getOrders());
    }

    @Override
    public Order getOrder(int userID, int orderID) throws IdNotFound, OrderNotRelated {
        User user = get(userID);
        return user.getOrders().stream()
                .filter(x -> x.getId() == orderID).findAny().orElseThrow(OrderNotRelated::new);
    }


    @Override
    public Tag mostUsedTag() throws IdNotFound, NoExistingOrders, NoTagInOrder {
        Tag tag = null;
        Order order = orderRepository.getHighestCostOrder();
        if (order != null) {
            Map<Integer, Integer> currentTags = mapCurrentTags();

            order.getCertificates().forEach(c ->
                    c.getAssociatedTags()
                            .forEach(t -> currentTags.merge(t.getId(), 1, Integer::sum)));

            int tagID = getMaxTagID(currentTags);

            if (tagID > 0) {
                tag = tagRepository.findById(tagID).orElseThrow(IdNotFound::new);
            } else {
                throw new NoTagInOrder();
            }
        } else {
            throw new NoExistingOrders();
        }

        return tag;
    }

    private Integer getMaxTagID(Map<Integer, Integer> tags) {
        return tags
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .get()
                .getValue();
    }

    private Map<Integer, Integer> mapCurrentTags() {
        Map<Integer, Integer> currentTags = new HashMap<>();
        tagRepository.findAll().forEach(t -> currentTags.put(t.getId(), 0));
        return currentTags;
    }

}
