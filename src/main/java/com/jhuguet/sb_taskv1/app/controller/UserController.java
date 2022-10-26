package com.jhuguet.sb_taskv1.app.controller;

import com.jhuguet.sb_taskv1.app.exceptions.IdNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.OrderNotRelated;
import com.jhuguet.sb_taskv1.app.models.Order;
import com.jhuguet.sb_taskv1.app.models.User;
import com.jhuguet.sb_taskv1.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final Logger logger = Logger.getLogger(GiftCertificateController.class.getName());
    private final UserService userService;

    @Autowired
    public UserController(UserService service) {
        this.userService = service;
    }

    @ResponseBody
    @GetMapping
    public List<User> getAll() {
        logger.info("Retrieving all Users");
        return userService.getAll();
    }

    @ResponseBody
    @GetMapping("/{id}")
    public User get(@PathVariable String id) throws IdNotFound {
        logger.info("Retrieving User: " + id);
        return userService.get(Integer.parseInt(id));
    }

    @ResponseBody
    @GetMapping("/{userID}/orders/{orderID}")
    public Order getOrder(@PathVariable(name = "userID") String userID,
                          @PathVariable(name = "orderID") String orderID) throws IdNotFound, OrderNotRelated {
        logger.info("Retrieving order " + orderID + " of user: " + userID);
        return userService.getOrder(Integer.parseInt(userID), Integer.parseInt(orderID));
    }

    @ResponseBody
    @GetMapping("/{id}/orders")
    public List<Order> getOrders(@PathVariable String id) throws IdNotFound {
        logger.info("Retrieving orders of user " + id);
        return userService.getOrders(Integer.parseInt(id));
    }

    @ResponseBody
    @GetMapping("/{userID}/highestCostOrder")
    public Order highestCostOrder(@PathVariable int userID) throws IdNotFound {
        return userService.highestCostOrder(userID);
    }
}
