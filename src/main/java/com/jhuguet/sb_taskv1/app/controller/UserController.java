package com.jhuguet.sb_taskv1.app.controller;

import com.jhuguet.sb_taskv1.app.exceptions.BaseException;
import com.jhuguet.sb_taskv1.app.exceptions.IdNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.InvalidInputInformation;
import com.jhuguet.sb_taskv1.app.exceptions.OrderNotRelated;
import com.jhuguet.sb_taskv1.app.exceptions.PageNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.WrongSortOrder;
import com.jhuguet.sb_taskv1.app.models.Order;
import com.jhuguet.sb_taskv1.app.models.User;
import com.jhuguet.sb_taskv1.app.pages.PageResponse;
import com.jhuguet.sb_taskv1.app.services.CustomUserDetailsService;
import com.jhuguet.sb_taskv1.app.services.UserService;
import com.jhuguet.sb_taskv1.app.web.utils.ControllerJwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.Principal;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Public API User controller
 */
@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final Logger logger = Logger.getLogger(GiftCertificateController.class.getName());
    private final PageResponse pageResponse = new PageResponse();
    private final ControllerJwtUtils utils = new ControllerJwtUtils();
    private final UserService userService;
    private final CustomUserDetailsService userDetailsService;

    @Autowired
    public UserController(UserService service, CustomUserDetailsService userDetailsService) {
        this.userService = service;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Will gives all existing Users in database
     *
     * @param page Page requested to see
     * @param size Given size of a page
     * @param sort Sorting value of ascendant or descendant order
     * @return EntityModel of Page containing Users
     * @throws InvalidInputInformation Exception thrown when given ID is incorrectly entered
     * @throws PageNotFound            Exception thrown when page requested doesn't exist
     */
    @GetMapping
    public EntityModel<Page<User>> getAll(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "3") int size,
                                          @RequestParam(defaultValue = "asc") String sort) throws PageNotFound,
            InvalidInputInformation, WrongSortOrder {
        pageResponse.validateInput(page, size);
        Page<User> users = userService.getAll(pageResponse.giveDynamicPageable(page, size, sort));
        logger.info("Retrieving all users");

        return EntityModel.of(users, linkTo(methodOn(UserController.class).getAll(page, size, sort)).withSelfRel());
    }

    /**
     * Will retrieve User from Database
     *
     * @return will return pertaining User retrieved from DB
     * @throws IdNotFound Exception thrown when given ID is not found
     */
    @GetMapping("/session")
    public User get(Principal principal) throws BaseException {
        int id = userDetailsService.getUserByUsername(principal.getName()).getId();

        logger.info("Retrieving User: " + id);
        return userService.get(id);
    }


    /**
     * Will look for specific order related to user
     *
     * @param orderId Order ID to look for in database
     * @return Order related to user
     * @throws IdNotFound      Exception thrown when given ID is not found
     * @throws OrderNotRelated Exception thrown when given order ID is not associated to user
     */
    @GetMapping("/orders/{orderId}")
    public Order getOrder(@PathVariable int orderId, Principal principal) throws BaseException, IOException {
        int id = userDetailsService.getUserByUsername(principal.getName()).getId();
        logger.info("Retrieving order " + orderId + " of user: " + id);

        return userService.getOrder(id, orderId);
    }

    /**
     * Will look for all orders associated to a user
     *
     * @return List of orders related to User
     * @throws IdNotFound Exception thrown when given ID is not found
     */
    @GetMapping("/orders")
    public EntityModel<Page<Order>> getOrders(@RequestParam(defaultValue = "1") int page,
                                              @RequestParam(defaultValue = "3") int size,
                                              @RequestParam(defaultValue = "asc") String sort,
                                              Principal principal) throws BaseException, IOException {
        int id = userDetailsService.getUserByUsername(principal.getName()).getId();
        Page<Order> orders = userService.getOrders(id, pageResponse.giveDynamicPageable(page, size, sort));

        logger.info("Retrieving orders from user: " + id);

        return EntityModel.of(orders,
                linkTo(methodOn(UserController.class).getOrders(page, size, sort, principal)).withSelfRel());
    }

}
