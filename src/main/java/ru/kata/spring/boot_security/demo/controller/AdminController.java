package ru.kata.spring.boot_security.demo.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserServiceImpl userServiceImpl;
    private final RoleService roleService;


    @Autowired
    public AdminController(UserServiceImpl userServiceImpl, RoleService roleService) {
        this.userServiceImpl = userServiceImpl;
        this.roleService = roleService;
    }

    @GetMapping
    public String adminHome(Model model) {
        model.addAttribute("users", userServiceImpl.getAllUsers());
        model.addAttribute("newUser", new User());
        model.addAttribute("roles", roleService.getAllRoles());
        return "admin";
    }

    @GetMapping("/new")
    public String newUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.getAllRoles());
        return "new-user";
    }

    @GetMapping("/users")
    @ResponseBody
    public List<User> getAllUsers() {
        return userServiceImpl.getAllUsers();
    }


    @GetMapping("/users/{id}")
    @ResponseBody
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userServiceImpl.findUserById(id);
        if (user != null && user.getId() != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        User user = userServiceImpl.findUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.getAllRoles());
        return "edit-user";
    }


 /*   @PostMapping("/users")
    @ResponseBody
    public ResponseEntity<String> createUser(@RequestBody User user) {
        boolean success = userServiceImpl.saveUser(user);
        if (success) {
            return ResponseEntity.ok("User created successfully");
        } else {
            return ResponseEntity.badRequest().body("Username already exists");
        }
    }*/


    @DeleteMapping("/users/{id}")
    @ResponseBody
    public String deleteUser(@PathVariable Long id) {
        userServiceImpl.deleteUser(id);
            return "redirect:/admin";
        }



    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") @Valid User user,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("roles", roleService.getAllRoles());
            return "edit-user";
        }
        userServiceImpl.updateUser(user);
        return "redirect:/admin";
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute("newUser") @Valid User user,
                             BindingResult result, Model model) {
        user.setUsername(user.getEmail());
        if (result.hasErrors()) {
            model.addAttribute("users", userServiceImpl.getAllUsers());
            model.addAttribute("roles", roleService.getAllRoles());
            return "admin";
        }
        boolean success = userServiceImpl.createUser(user);
        if (!success) {
            model.addAttribute("error", "Username already exists");
            model.addAttribute("users", userServiceImpl.getAllUsers());
            model.addAttribute("roles", roleService.getAllRoles());
            return "admin";
        }
        return "redirect:/admin";
    }
  }