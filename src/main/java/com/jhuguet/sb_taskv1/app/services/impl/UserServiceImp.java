package com.jhuguet.sb_taskv1.app.services.impl;

import com.jhuguet.sb_taskv1.app.exceptions.IdNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.OrderNotRelated;
import com.jhuguet.sb_taskv1.app.models.Order;
import com.jhuguet.sb_taskv1.app.models.User;
import com.jhuguet.sb_taskv1.app.repositories.UserRepository;
import com.jhuguet.sb_taskv1.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class UserServiceImp implements UserService {

    private final UserRepository repository;

    @Autowired
    public UserServiceImp(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User get(int id) throws IdNotFound {
        return repository.findById(id).orElseThrow(IdNotFound::new);
    }

    @Override
    public List<User> getAll() {
        return repository.findAll();
    }

    @Override
    public List<Order> getOrders(int id) throws IdNotFound {
        User user = get(id);
        return new ArrayList<>(user.getOrders());
    }


    @Override
    public Order highestCostOrder(int id) throws IdNotFound {
        Comparator<Order> comparator = Comparator.comparing(Order::getCost);

        getAll().forEach(user -> {
            User maxCostUser = null;
            Order maxCostOrder = user.getOrders().stream().max(comparator).get();
            try {
                maxCostUser = get(maxCostOrder.getUser().getId());
            } catch (IdNotFound e) {
                throw new RuntimeException(e);
            }


        });

        return new Order();
    }

    @Override
    public Order getOrder(int userID, int orderID) throws IdNotFound, OrderNotRelated {
        User user = get(userID);
        return user.getOrders().stream()
                .filter(x -> x.getId() == orderID).findAny().orElseThrow(OrderNotRelated::new);
    }

}
