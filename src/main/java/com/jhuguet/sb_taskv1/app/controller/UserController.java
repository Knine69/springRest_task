package com.jhuguet.sb_taskv1.app.controller;

import com.jhuguet.sb_taskv1.app.exceptions.IdNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.InvalidInputInformation;
import com.jhuguet.sb_taskv1.app.exceptions.NoExistingOrders;
import com.jhuguet.sb_taskv1.app.exceptions.NoTagInOrder;
import com.jhuguet.sb_taskv1.app.exceptions.OrderNotRelated;
import com.jhuguet.sb_taskv1.app.models.Order;
import com.jhuguet.sb_taskv1.app.models.Tag;
import com.jhuguet.sb_taskv1.app.models.User;
import com.jhuguet.sb_taskv1.app.pages.PageResponse;
import com.jhuguet.sb_taskv1.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final Logger logger = Logger.getLogger(GiftCertificateController.class.getName());
    private final PageResponse pageResponse = new PageResponse();
    private final UserService userService;

    @Autowired
    public UserController(UserService service) {
        this.userService = service;
    }

    @ResponseBody
    @GetMapping
    public Page<User> getAll(@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "3") int size,
                             @RequestParam(defaultValue = "true") boolean asc) throws InvalidInputInformation {
        pageResponse.validateInput(page, size);
        return userService.getAll(pageResponse.giveDynamicPageable(page, size, asc));
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
    @GetMapping("/mostWidelyUsedTag")
    public Tag highestCostOrder() throws IdNotFound, NoExistingOrders, NoTagInOrder {
        return userService.mostUsedTag();
    }
}
