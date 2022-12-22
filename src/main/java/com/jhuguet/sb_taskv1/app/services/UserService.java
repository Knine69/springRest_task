package com.jhuguet.sb_taskv1.app.services;

import com.jhuguet.sb_taskv1.app.exceptions.IdNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.MissingEntity;
import com.jhuguet.sb_taskv1.app.exceptions.MissingRequiredFields;
import com.jhuguet.sb_taskv1.app.exceptions.OrderNotRelated;
import com.jhuguet.sb_taskv1.app.exceptions.PageNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.UnqualifiedAuthority;
import com.jhuguet.sb_taskv1.app.models.Order;
import com.jhuguet.sb_taskv1.app.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    User get(int id) throws IdNotFound;

    Page<User> getAll(Pageable pageable) throws PageNotFound;

    Order getOrder(int userId, int orderId) throws IdNotFound, OrderNotRelated;

    Page<Order> getOrders(int id, Pageable pageable) throws IdNotFound;

    void signIn(User user) throws MissingEntity, MissingRequiredFields, UnqualifiedAuthority;

}
