package com.jhuguet.sb_taskv1.app.services;

import com.jhuguet.sb_taskv1.app.exceptions.IdNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.OrderNotRelated;
import com.jhuguet.sb_taskv1.app.models.Order;
import com.jhuguet.sb_taskv1.app.models.User;

import java.util.List;
import java.util.Set;

public interface UserService {
    User get(int id) throws IdNotFound;

    List<User> getAll();

    List<Order> getOrders(int id) throws IdNotFound;

    Order getOrder(int userID, int orderID) throws IdNotFound, OrderNotRelated;
}
