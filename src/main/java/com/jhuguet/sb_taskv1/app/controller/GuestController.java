package com.jhuguet.sb_taskv1.app.controller;

import com.jhuguet.sb_taskv1.app.exceptions.MissingEntity;
import com.jhuguet.sb_taskv1.app.exceptions.MissingUserFields;
import com.jhuguet.sb_taskv1.app.models.User;
import com.jhuguet.sb_taskv1.app.services.GuestService;
import com.jhuguet.sb_taskv1.app.web.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@ControllerAdvice(basePackages = "com.jhuguet.sb_taskv1.app")
@RequestMapping("/")
public class GuestController {

    private final GuestService guestService;
    private Utils utils = new Utils();

    @Autowired
    public GuestController(GuestService guestService) {
        this.guestService = guestService;
    }

    @GetMapping("home")
    public String homePage(@ModelAttribute User user, Model model) {

        utils.setUpModel(model);
        guestService.authenticate(user, model);
        return "Home";
    }

    @GetMapping({"login", ""})
    public String loginPage(Model model) {
        model.addAttribute("user", new User());
        return "Login";
    }

    @GetMapping("signup")
    public String signUp(Model model) {
        model.addAttribute("user", new User());
        return "SignUp";
    }


    @PostMapping
    public String saveUser(@ModelAttribute User user, Model model) throws MissingEntity, MissingUserFields {
        guestService.signUp(user);
        model.addAttribute("user", user);
        return "Home";
    }
}
