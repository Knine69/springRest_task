package com.jhuguet.sb_taskv1.app.services;

import com.jhuguet.sb_taskv1.app.exceptions.IdNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.NoExistingOrders;
import com.jhuguet.sb_taskv1.app.exceptions.NoTagInOrder;
import com.jhuguet.sb_taskv1.app.exceptions.OrderNotRelated;
import com.jhuguet.sb_taskv1.app.models.Order;
import com.jhuguet.sb_taskv1.app.models.Tag;
import com.jhuguet.sb_taskv1.app.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    User get(int id) throws IdNotFound;

    Page<User> getAll(Pageable pageable);

    Order getOrder(int userID, int orderID) throws IdNotFound, OrderNotRelated;

    List<Order> getOrders(int id) throws IdNotFound;

    Tag mostUsedTag() throws IdNotFound, NoExistingOrders, NoTagInOrder;

}
