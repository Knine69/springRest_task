package com.jhuguet.sb_taskv1.app.web;

import com.jhuguet.sb_taskv1.app.models.User;
import org.springframework.ui.Model;

import java.util.ArrayList;

public class Utils {

    public void setUpModel(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("lookAtTable", true);
        model.addAttribute("orderList", new ArrayList<>());
        model.addAttribute("totalElements", 1);
        model.addAttribute("totalPages", 3);
        model.addAttribute("pageNumber", 0);
        model.addAttribute("pageSize", 3);
    }
}
