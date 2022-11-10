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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, TagRepository tagRepository,
                           OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
        this.orderRepository = orderRepository;
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
    public Page<Order> getOrders(int id, Pageable pageable) throws IdNotFound {
        User user = get(id);
        return new PageImpl<>(new ArrayList<>(user.getOrders()), pageable, user
                .getOrders()
                .size());
    }

    @Override
    public User get(int id) throws IdNotFound {
        return userRepository
                .findById(id)
                .orElseThrow(IdNotFound::new);
    }

    @Override
    public Order getOrder(int userId, int orderId) throws IdNotFound, OrderNotRelated {
        User user = get(userId);
        return user
                .getOrders()
                .stream()
                .filter(x -> x.getId() == orderId)
                .findAny()
                .orElseThrow(OrderNotRelated::new);
    }


    @Override
    public Tag mostUsedTag() throws NoExistingOrders, NoTagInOrder, IdNotFound {
        Tag tag;
        Order order = orderRepository.getHighestCostOrder();
        if (order != null) {
            tag = countTagIterations(order);
        } else {
            throw new NoExistingOrders();
        }

        return tag;
    }

    private Tag countTagIterations(Order order) throws IdNotFound, NoTagInOrder {
        Tag tag;
        Map<Integer, Integer> currentTags = mapCurrentTags();

        order
                .getCertificates()
                .forEach(c -> c
                        .getAssociatedTags()
                        .forEach(t -> currentTags.merge(t.getId(), 1, Integer::sum)));

        int tagID = getMaxTagID(currentTags);

        if (tagID > 0) {
            tag = tagRepository
                    .findById(tagID)
                    .orElseThrow(IdNotFound::new);
        } else {
            throw new NoTagInOrder();
        }
        return tag;
    }

    private Map<Integer, Integer> mapCurrentTags() {
        Map<Integer, Integer> currentTags = new HashMap<>();
        tagRepository
                .findAll()
                .forEach(t -> currentTags.put(t.getId(), 0));
        return currentTags;
    }

    private Integer getMaxTagID(Map<Integer, Integer> tags) {
        return tags
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .get()
                .getValue();
    }

    @Override
    public void assignAttributes(Page<Order> orders, int size, int page, Model model) {
        model.addAttribute("lookAtTable", true);
        model.addAttribute("orderList", orders.getContent());
        model.addAttribute("totalElements", orders.getTotalElements());
        model.addAttribute("totalPages", orders.getTotalPages());
        model.addAttribute("pageNumber", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("user", orders
                .getContent()
                .get(0)
                .getUser());

    }

}
