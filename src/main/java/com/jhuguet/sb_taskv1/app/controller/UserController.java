package com.jhuguet.sb_taskv1.app.controller;

import com.jhuguet.sb_taskv1.app.exceptions.BaseException;
import com.jhuguet.sb_taskv1.app.exceptions.IdNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.InvalidInputInformation;
import com.jhuguet.sb_taskv1.app.exceptions.NoExistingOrders;
import com.jhuguet.sb_taskv1.app.exceptions.NoTagInOrder;
import com.jhuguet.sb_taskv1.app.exceptions.OrderNotRelated;
import com.jhuguet.sb_taskv1.app.exceptions.PageNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.WrongSortOrder;
import com.jhuguet.sb_taskv1.app.models.Order;
import com.jhuguet.sb_taskv1.app.models.Tag;
import com.jhuguet.sb_taskv1.app.models.User;
import com.jhuguet.sb_taskv1.app.pages.PageResponse;
import com.jhuguet.sb_taskv1.app.services.UserService;
import com.jhuguet.sb_taskv1.app.web.utils.ControllerJwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;
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

    @Autowired
    public UserController(UserService service) {
        this.userService = service;
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
    @GetMapping("/current")
    public User get(@RequestHeader Map<String, String> headers) throws IOException, BaseException {
        String jwt = utils.retrieveJwt(headers);
        int id = userService.getIdFromJwt(jwt);

        logger.info("Retrieving User: " + id);
        userService.checkIdentity(jwt, false);
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
    public Order getOrder(@PathVariable int orderId, @RequestHeader Map<String, String> headers) throws BaseException,
            IOException {
        String jwt = utils.retrieveJwt(headers);
        int id = userService.getIdFromJwt(jwt);
        userService.checkIdentity(headers.get("authorization"), false);
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
                                              @RequestHeader Map<String, String> headers) throws BaseException,
            IOException {
        String jwt = utils.retrieveJwt(headers);
        int id = userService.getIdFromJwt(jwt);
        Page<Order> orders = userService.getOrders(id, pageResponse.giveDynamicPageable(page, size, sort));

        userService.checkIdentity(jwt, false);
        logger.info("Retrieving orders from user: " + id);

        return EntityModel.of(orders,
                linkTo(methodOn(UserController.class).getOrders(page, size, sort, headers)).withSelfRel());
    }

    /**
     * Will look for the most used Tag of the user with the highest order cost
     *
     * @return Tag most widely used
     * @throws IdNotFound       Exception thrown when given ID is not found
     * @throws NoExistingOrders There are no orders associated to this user
     * @throws NoTagInOrder     There are no Tags associated to the orders' certificates
     */
    @GetMapping("/mostWidelyUsedTag")
    public Tag highestCostOrder(@RequestHeader Map<String, String> headers) throws BaseException {
        return userService.mostUsedTag();
    }
}
