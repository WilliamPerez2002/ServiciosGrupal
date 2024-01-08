package com.example.demo.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
public class AppController {



    @GetMapping("/login")
    public String loginPage(@RequestParam(name = "x", required = false) String error, @RequestParam(name = "logout", required = false) String logout,  Model model) {
        if (error != null && !error.isEmpty()) {
            model.addAttribute("x", "M");
        }
        if (logout != null && !logout.isEmpty()) {
            model.addAttribute("logout", "M");
        }



        return "login.html";
    }

    @GetMapping("/usuarios")
    public String userPage(Model model) {
        return "user.html";
    }

    @GetMapping("/")
    public String principalPage(Model model) {
        return "index.html";
    }

    @GetMapping("/error")
    public String errorPage(Model model){
        return "error.html";
    }



}
