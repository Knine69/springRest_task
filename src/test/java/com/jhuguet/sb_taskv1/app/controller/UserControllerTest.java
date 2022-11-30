package com.jhuguet.sb_taskv1.app.controller;

import com.jhuguet.sb_taskv1.app.exceptions.BaseException;
import com.jhuguet.sb_taskv1.app.exceptions.IdNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.InvalidInputInformation;
import com.jhuguet.sb_taskv1.app.exceptions.OrderNotRelated;
import com.jhuguet.sb_taskv1.app.exceptions.PageNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.UsernameNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.WrongSortOrder;
import com.jhuguet.sb_taskv1.app.pages.PageResponse;
import com.jhuguet.sb_taskv1.app.repositories.OrderRepository;
import com.jhuguet.sb_taskv1.app.repositories.TagRepository;
import com.jhuguet.sb_taskv1.app.repositories.UserRepository;
import com.jhuguet.sb_taskv1.app.services.CustomUserDetailsService;
import com.jhuguet.sb_taskv1.app.services.impl.UserServiceImpl;
import com.jhuguet.sb_taskv1.app.services.utils.SetUpUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    private static final PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
    @Autowired
    private static final UserServiceImpl userService = new UserServiceImpl(userRepository, passwordEncoder);
    private static final CustomUserDetailsService userDetailsService = Mockito.mock(CustomUserDetailsService.class);
    private final UserController controller = new UserController(userService, userDetailsService);

    @AfterEach
    public void resetMocks() throws WrongSortOrder, UsernameNotFound {
        setMocks();
    }

    @BeforeAll
    private static void setMocks() throws WrongSortOrder, UsernameNotFound {
        when(userRepository.existsById(anyInt())).thenReturn(true);
        when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(utils.sampleUser()));
        when(userRepository.findAll(any(Pageable.class))).thenReturn(utils.samplePageUsers());

        Mockito.when(userDetailsService.getUserByUsername(Mockito.anyString())).thenReturn(utils.sampleUser());

        when(orderRepository.existsById(anyInt())).thenReturn(true);
        when(orderRepository.findById(anyInt())).thenReturn(Optional.ofNullable(utils.sampleOrder()));
        when(orderRepository.findAll()).thenReturn(utils.sampleOrders());

        when(tagRepository.existsById(anyInt())).thenReturn(true);
        when(tagRepository.findById(anyInt())).thenReturn(Optional.ofNullable(utils.sampleTag()));

        when(pageResponse.giveDynamicPageable(anyInt(), anyInt(), anyString())).thenReturn(PageRequest.of(1, 1));
    }

    @Test
    void getAllUsersIfPagingParamsAreCorrectlyPassed() throws InvalidInputInformation, PageNotFound, WrongSortOrder {
        assertEquals(utils.sampleOrders().size(),
                Objects.requireNonNull(controller.getAll(1, 1, "asc").getContent()).getTotalElements());
    }

    @Test
    void getAllUsersInvalidInputInformationIfPagingParamsAreNotComplying() {
        assertThrows(InvalidInputInformation.class,
                () -> Objects.requireNonNull(controller.getAll(0, 0, "asc").getContent()).getTotalElements());
    }

    @Test
    void getAllUsersErrorIfPageNotFound() {
        assertThrows(PageNotFound.class,
                () -> Objects.requireNonNull(controller.getAll(10, 1, "asc").getContent()).getTotalElements());
    }

    @Test
    void getUserWhenGivingExistingId() throws BaseException {
        assertEquals(utils.sampleUser().getId(), controller
                .get(new AnonymousAuthenticationToken("key", utils.sampleUserDetails(),
                        new ArrayList<>(List.of(new SimpleGrantedAuthority("USER"))))).getId());
    }

    @Test
    void getUserWhenGivingNonExistingIdNotFound() {
        toggleMockIdFound();
        assertThrows(IdNotFound.class, () -> controller.get(
                new AnonymousAuthenticationToken("key", utils.sampleUserDetails(),
                        new ArrayList<>(List.of(new SimpleGrantedAuthority("USER"))))));
    }

    private void toggleMockIdFound() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());
    }

    @Test
    void getOrderRelatedToUserIfGivenCorrectlySetOfIds() throws BaseException, IOException {
        assertEquals(utils.sampleOrder().getId(), controller.getOrder(1,
                new AnonymousAuthenticationToken("key", utils.sampleUserDetails(),
                        new ArrayList<>(List.of(new SimpleGrantedAuthority("USER"))))).getId());
    }

    @Test
    void getOrderExceptionIfNotRelatedToUser() {
        assertThrows(OrderNotRelated.class, () -> controller.getOrder(10,
                new AnonymousAuthenticationToken("key", utils.sampleUserDetails(),
                        new ArrayList<>(List.of(new SimpleGrantedAuthority("USER"))))));
    }

    @Test
    void getOrderRelatedToUserExceptionIfIdIsNonExistent() {
        toggleMockIdFound();
        assertThrows(IdNotFound.class, () -> controller.getOrder(1,
                new AnonymousAuthenticationToken("key", utils.sampleUserDetails(),
                        new ArrayList<>(List.of(new SimpleGrantedAuthority("USER"))))));
    }

    @Test
    void getAllOrdersRelatedToUserIfUserExists() throws IOException, BaseException {
        assertEquals(utils.sampleOrders().size(), Objects.requireNonNull(controller.getOrders(1, 1, "asc",
                                                                 new AnonymousAuthenticationToken("key", utils.sampleUserDetails(),
                                                                         new ArrayList<>(List.of(new SimpleGrantedAuthority("USER"))))).getContent())
                                                         .getTotalElements());
    }
}
