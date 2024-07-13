package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String adminPage(Model model, Principal principal) {
        model.addAttribute("authUser", userService.findByEmail(principal.getName()));
        model.addAttribute("users", userService.showUsers());
        model.addAttribute("newUser", new User());
        model.addAttribute("roles", userService.listRoles());
        return "admin";
    }

    @PostMapping("/addNewUser")
    public String saveUser(@ModelAttribute("user") User user, BindingResult result) {

        userService.save(user);
        return "redirect:/admin";
    }

    @PatchMapping("/update/{id}")
    public String updateUser(@PathVariable("id") @ModelAttribute("user") User user, BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/admin";
        }
        userService.update(user);
        return "admin";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}