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
import java.util.List;

@Service
public class UserServiceImp implements UserService {

    private UserRepository repository;

    @Autowired
    public UserServiceImp(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User get(int id) throws IdNotFound {
        User user = repository.findById(id).orElseThrow(IdNotFound::new);
        updateUserOrderCost(user);
        return user;
    }

    @Override
    public List<User> getAll() {
        List<User> users = repository.findAll();
        updateAllUsersOrderCost(users);
        return users;
    }

    @Override
    public List<Order> getOrders(int id) throws IdNotFound {
        User user = get(id);
        return new ArrayList<>(user.getOrders());
    }

    @Override
    public Order getOrder(int userID, int orderID) throws IdNotFound, OrderNotRelated {
        User user = get(userID);
        Order order = user.getOrders().stream()
                .filter(x -> x.getId() == orderID).findAny().orElseThrow(OrderNotRelated::new);
        return order;
    }

    private void updateUserOrderCost(User user) {
        user.getOrders().forEach(o -> o.calculateCost());
    }

    private void updateAllUsersOrderCost(List<User> users) {
        users.forEach(u -> updateUserOrderCost(u));
    }
}
