package com.jhuguet.sb_taskv1.app.services;

import com.jhuguet.sb_taskv1.app.exceptions.IdNotFound;
import com.jhuguet.sb_taskv1.app.models.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    User get(int id) throws IdNotFound;
    List<User> getUsers();
}
