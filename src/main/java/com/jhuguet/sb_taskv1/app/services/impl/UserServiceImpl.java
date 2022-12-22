package com.jhuguet.sb_taskv1.app.services.impl;

import com.jhuguet.sb_taskv1.app.exceptions.IdNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.MissingEntity;
import com.jhuguet.sb_taskv1.app.exceptions.MissingRequiredFields;
import com.jhuguet.sb_taskv1.app.exceptions.OrderNotRelated;
import com.jhuguet.sb_taskv1.app.exceptions.PageNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.UnqualifiedAuthority;
import com.jhuguet.sb_taskv1.app.models.Order;
import com.jhuguet.sb_taskv1.app.models.User;
import com.jhuguet.sb_taskv1.app.repositories.UserRepository;
import com.jhuguet.sb_taskv1.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value(value = "${kafka.topic.name}")
    private String topicName;
    private final KafkaTemplate<String, String> kafkaTemplate;


    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           KafkaTemplate<String, String> kafkaTemplate) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public Page<User> getAll(Pageable pageable) throws PageNotFound {

        Page<User> page = userRepository.findAll(pageable);

        if (page.getTotalPages() <= pageable.getPageNumber()) {
            throw new PageNotFound();
        }

        kafkaTemplate.send(topicName, "Getting all users");

        return page;
    }

    @Override
    public Page<Order> getOrders(int id, Pageable pageable) throws IdNotFound {
        User user = get(id);

        kafkaTemplate.send(topicName, "Getting all orders from user: " + id);
        return new PageImpl<>(new ArrayList<>(user.getOrders()), pageable, user.getOrders().size());
    }

    @Override
    public User get(int id) throws IdNotFound {
        kafkaTemplate.send(topicName, "Getting user: " + id);
        return userRepository.findById(id).orElseThrow(IdNotFound::new);
    }

    @Override
    public Order getOrder(int userId, int orderId) throws IdNotFound, OrderNotRelated {
        kafkaTemplate.send(topicName, "Getting order: " + orderId + "from user: " + userId);
        User user = get(userId);

        return user.getOrders().stream().filter(x -> x.getId() == orderId).findAny().orElseThrow(OrderNotRelated::new);
    }

    @Override
    public void signIn(User user) throws MissingEntity, MissingRequiredFields, UnqualifiedAuthority {
        if (!Objects.isNull(user)) {
            userEntityValidations(user);
            userRepository.save(assembleUserToSave(user));
            kafkaTemplate.send(topicName, "Signing in with user: " + user.getUsername());
        } else {
            throw new MissingEntity();
        }
    }

    private void userEntityValidations(User user) throws MissingRequiredFields, UnqualifiedAuthority {
        if (user.getPassword().equals("") || user.getUsername().equals("")) {
            throw new MissingRequiredFields();
        }

        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new UnqualifiedAuthority();
        }
    }

    private User assembleUserToSave(User user) {
        return new User(user.getUsername(), user.getEmail(), passwordEncoder.encode(user.getPassword()),
                new HashSet<>(), "USER");
    }

}
