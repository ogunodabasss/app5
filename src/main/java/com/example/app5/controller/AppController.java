package com.example.app5.controller;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {

    @GetMapping(value = {"/", "/index"})
    public String index(@NotNull Model model) {
        System.err.println(model);
        model.addAttribute("title", "Spring App");
        return "index";
    }
}
