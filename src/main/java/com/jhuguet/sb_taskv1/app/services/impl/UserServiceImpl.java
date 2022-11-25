package com.jhuguet.sb_taskv1.app.services.impl;

import com.jhuguet.sb_taskv1.app.exceptions.IdNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.MissingEntity;
import com.jhuguet.sb_taskv1.app.exceptions.MissingRequiredFields;
import com.jhuguet.sb_taskv1.app.exceptions.NotAuthorized;
import com.jhuguet.sb_taskv1.app.exceptions.OrderNotRelated;
import com.jhuguet.sb_taskv1.app.exceptions.PageNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.UnqualifiedAuthority;
import com.jhuguet.sb_taskv1.app.exceptions.WrongCredentials;
import com.jhuguet.sb_taskv1.app.models.Order;
import com.jhuguet.sb_taskv1.app.models.User;
import com.jhuguet.sb_taskv1.app.repositories.OrderRepository;
import com.jhuguet.sb_taskv1.app.repositories.TagRepository;
import com.jhuguet.sb_taskv1.app.repositories.UserRepository;
import com.jhuguet.sb_taskv1.app.services.UserService;
import com.jhuguet.sb_taskv1.app.services.authorize.RequestAuthorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final OrderRepository orderRepository;
    private final CustomUserDetailsServiceImpl detailsService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           TagRepository tagRepository,
                           OrderRepository orderRepository,
                           CustomUserDetailsServiceImpl detailsService,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
        this.orderRepository = orderRepository;
        this.detailsService = detailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Page<User> getAll(Pageable pageable) throws PageNotFound {

        Page<User> page = userRepository.findAll(pageable);

        if (page.getTotalPages() <= pageable.getPageNumber()) {
            throw new PageNotFound();
        }

        return page;
    }

    @Override
    public Page<Order> getOrders(int id, Pageable pageable) throws IdNotFound {
        User user = get(id);
        return new PageImpl<>(new ArrayList<>(user.getOrders()), pageable, user.getOrders().size());
    }

    @Override
    public User get(int id) throws IdNotFound {
        return userRepository.findById(id).orElseThrow(IdNotFound::new);
    }

    @Override
    public Order getOrder(int userId, int orderId) throws IdNotFound, OrderNotRelated {
        User user = get(userId);
        return user.getOrders().stream().filter(x -> x.getId() == orderId).findAny().orElseThrow(OrderNotRelated::new);
    }

    @Override
    public void signIn(User user) throws MissingEntity, MissingRequiredFields, UnqualifiedAuthority {
        if (!Objects.isNull(user)) {
            userEntityValidations(user);
            userRepository.save(assembleUserToSave(user));
        } else {
            throw new MissingEntity();
        }
    }

    private void userEntityValidations(User user) throws MissingRequiredFields, UnqualifiedAuthority {
        if (user.getPassword().equals("") || user.getUsername().equals("")) {
            throw new MissingRequiredFields();
        }

        if (user.getUsername().equalsIgnoreCase("administrator")) {
            throw new UnqualifiedAuthority();
        }
    }

    private User assembleUserToSave(User user) {
        return new User(user.getUsername(), user.getEmail(), passwordEncoder.encode(user.getPassword()),
                new HashSet<>(), "USER");
    }

    public void checkIdentity(String jwt, boolean requiresAdmin) throws NotAuthorized, IOException,
            UnqualifiedAuthority {
        RequestAuthorization authorization = giveRequestAuthorization();
        if (requiresAdmin) {
            authorization.confirmRoles(jwt);
        }
    }

    private RequestAuthorization giveRequestAuthorization() {
        return new RequestAuthorization(detailsService);
    }

    @Override
    public Integer getIdFromJwt(String jwt) throws IOException {
        return Integer.valueOf(giveRequestAuthorization().givePropertyValue("id", jwt));
    }

}
