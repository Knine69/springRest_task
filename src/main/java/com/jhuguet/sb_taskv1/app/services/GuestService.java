package com.jhuguet.sb_taskv1.app.services;

import com.jhuguet.sb_taskv1.app.exceptions.MissingEntity;
import com.jhuguet.sb_taskv1.app.exceptions.MissingUserFields;
import com.jhuguet.sb_taskv1.app.models.User;
import org.springframework.ui.Model;

public interface GuestService {
    void getUsers(Model model, int page, int size, String sort);

    User signUp(User user) throws MissingEntity, MissingUserFields;

    void authenticate(User user, Model model);

}
