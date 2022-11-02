package com.jhuguet.sb_taskv1.app.controller;

import com.jhuguet.sb_taskv1.app.exceptions.IdNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.InvalidInputInformation;
import com.jhuguet.sb_taskv1.app.exceptions.NoExistingOrders;
import com.jhuguet.sb_taskv1.app.exceptions.NoTagInOrder;
import com.jhuguet.sb_taskv1.app.exceptions.OrderNotRelated;
import com.jhuguet.sb_taskv1.app.exceptions.PageNotFound;
import com.jhuguet.sb_taskv1.app.pages.PageResponse;
import com.jhuguet.sb_taskv1.app.repositories.OrderRepository;
import com.jhuguet.sb_taskv1.app.repositories.TagRepository;
import com.jhuguet.sb_taskv1.app.repositories.UserRepository;
import com.jhuguet.sb_taskv1.app.services.impl.UserServiceImpl;
import com.jhuguet.sb_taskv1.app.services.utils.SetUpUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class UserControllerTest {

    private static final SetUpUtils utils = new SetUpUtils();

    private static final PageResponse pageResponse = Mockito.mock(PageResponse.class);

    private static final OrderRepository orderRepository = Mockito.mock(OrderRepository.class);
    private static final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private static final TagRepository tagRepository = Mockito.mock(TagRepository.class);

    @Autowired
    private static final UserServiceImpl userService =
            new UserServiceImpl(userRepository, tagRepository, orderRepository);

    private final UserController controller = new UserController(userService);


    @BeforeAll
    private static void setMocks() throws NoExistingOrders {
        when(userRepository.existsById(anyInt())).thenReturn(true);
        when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(utils.sampleUser()));
        when(userRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(
                        new ArrayList<>(utils.sampleUsers()), PageRequest.of(1, 1), utils.sampleTags().size()
                ));

        when(orderRepository.existsById(anyInt())).thenReturn(true);
        when(orderRepository.findById(anyInt())).thenReturn(Optional.ofNullable(utils.sampleOrder()));
        when(orderRepository.getHighestCostOrder()).thenReturn(utils.sampleOrder());

        when(tagRepository.existsById(anyInt())).thenReturn(true);
        when(tagRepository.findById(anyInt())).thenReturn(Optional.ofNullable(utils.sampleTag()));

        when(pageResponse.giveDynamicPageable(anyInt(), anyInt(), anyString())).thenReturn(PageRequest.of(1, 1));
    }

    @AfterEach
    public void resetMocks() throws NoExistingOrders {
        setMocks();
    }

    @Test
    void getAllUsersIfPagingParamsAreCorrectlyPassed() throws InvalidInputInformation, PageNotFound {
        assertEquals(utils.sampleOrders().size(),
                controller.getAll(1, 1, "asc").getContent().getTotalElements());
    }

    @Test
    void getAllUsersInvalidInputInformationIfPagingParamsAreNotComplying() {
        assertThrows(InvalidInputInformation.class, () -> {
            controller.getAll(0, 0, "asc").getContent().getTotalElements();
        });
    }

    @Test
    void getAllUsersErrorIfPageNotFound() {
        assertThrows(PageNotFound.class, () -> {
            controller.getAll(10, 1, "asc").getContent().getTotalElements();
        });
    }

    @Test
    void getUserWhenGivingExistingId() throws IdNotFound {
        assertEquals(utils.sampleOrder().getId(), controller.get(1).getId());
    }

    @Test
    void getUserWhenGivingNonExistingIdNotFound() {
        toggleMockIdFound();
        assertThrows(IdNotFound.class, () -> controller.get(2));
    }

    @Test
    void getOrderRelatedToUserIfGivenCorrectlySetOfIds() throws OrderNotRelated, IdNotFound {
        assertEquals(utils.sampleOrder().getId(), controller.getOrder(1, 1).getId());
    }

    @Test
    void getOrderExceptionIfNotRelatedToUser() {
        assertThrows(OrderNotRelated.class, () -> controller.getOrder(1, 10));
    }

    @Test
    void getOrderRelatedToUserExceptionIfIdIsNonExistent() {
        toggleMockIdFound();
        assertThrows(IdNotFound.class, () -> controller.getOrder(1, 1));
    }

    @Test
    void getAllOrdersRelatedToAUserIfGivenIdExists() throws IdNotFound {
        assertEquals(utils.sampleOrders().size(), controller.getOrders(1).size());
    }

    @Test
    void getAllOrdersRelatedToAUserExceptionIfIdNotFound() {
        toggleMockIdFound();
        assertThrows(IdNotFound.class, () -> controller.getOrders(1));
    }

    @Test
    void highestCostOrderReturningMostUsedTagInHighestCostOrder() throws NoExistingOrders, NoTagInOrder, IdNotFound {
        assertEquals(utils.sampleTag().getId(), controller.highestCostOrder().getId());
    }

    @Test
    void highestCostOrderReturningMostUsedTagInHighestCostOrderNoExistingOrder() throws NoExistingOrders {
        when(orderRepository.getHighestCostOrder()).thenReturn(null);
        assertThrows(NoExistingOrders.class, () -> controller.highestCostOrder());
    }

    @Test
    void highestCostOrderReturningMostUsedTagInHighestCostOrderIdNotFound() throws NoExistingOrders {
        when(tagRepository.findById(anyInt())).thenReturn(Optional.ofNullable(null));
        assertThrows(IdNotFound.class, () -> controller.highestCostOrder());
    }

    private void toggleMockIdFound() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(null));
    }
}
