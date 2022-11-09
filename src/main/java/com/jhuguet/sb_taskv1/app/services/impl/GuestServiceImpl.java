package com.jhuguet.sb_taskv1.app.services.impl;

import com.jhuguet.sb_taskv1.app.exceptions.MissingEntity;
import com.jhuguet.sb_taskv1.app.exceptions.MissingUserFields;
import com.jhuguet.sb_taskv1.app.models.User;
import com.jhuguet.sb_taskv1.app.pages.PageResponse;
import com.jhuguet.sb_taskv1.app.repositories.UserRepository;
import com.jhuguet.sb_taskv1.app.services.GuestService;
import com.jhuguet.sb_taskv1.app.web.manager.DetailsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Objects;

@Service
public class GuestServiceImpl implements GuestService {

    private final UserRepository userRepository;
    private final DetailsManager detailsManager;


    @Autowired
    public GuestServiceImpl(UserRepository userRepository, DetailsManager detailsManager) {
        this.userRepository = userRepository;
        this.detailsManager = detailsManager;
    }

    @Override
    public void getUsers(Model model, int page, int size, String sort) {
        PageResponse pageResponse = new PageResponse();
        Page<User> users = userRepository.findAll(pageResponse.giveDynamicPageable(page, size, sort));

    }

    @Override
    public User signUp(User user) throws MissingEntity, MissingUserFields {
        validateUser(user);
        userRepository.save(user);
        return user;
    }

    private void validateUser(User user) throws MissingEntity, MissingUserFields {
        if (!Objects.isNull(user)) {
            if (user
                    .getPassword()
                    .equals("") || user
                    .getUsername()
                    .equals("") || user
                    .getEmail()
                    .equals("")) {
                throw new MissingUserFields();
            }
            userRepository.save(user);
            detailsManager.saveUserDetails(user, false);
        } else {
            throw new MissingEntity();
        }
    }

    @Override
    public String authenticate(User user, Model model) {
        if (detailsManager.validateUserExists(user)) {
            model.addAttribute("error", false);
            model.addAttribute("username", user.getUsername());
            return "Home";
        }
        model.addAttribute("error", true);
        return "Login";
    }

}
