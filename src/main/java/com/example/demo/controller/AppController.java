package com.example.demo.controller;

import com.example.demo.Service.RoleService;
import com.example.demo.Service.UserService;
import com.example.demo.models.Role;
import com.example.demo.models.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Controller
public class AppController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AppController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("")
    public String viewHomePage() {
        return "index";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.getAllRoles());
        return "signup_form";
    }

    @PostMapping("/process_register")
    public String processRegister(@ModelAttribute User user,
                                  @RequestParam(value = "roless",
                                          required = false,
                                          defaultValue = "ROLE_USER") Set<String> roles) {

        Set<Role> setRoles = roleService.getSetRoles(roles);
        user.setRoles(setRoles);

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        userRepo.save(user);

        return "register_success";
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> listUsers = userService.getAllUsers();
        model.addAttribute("listUsers", listUsers);

        return "users";
    }


    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable("id") Long id, Model model) {
        User user = userService.getById(id);
        List<Role> listRoles = roleService.getAllRoles();
        model.addAttribute("user", user);
        model.addAttribute("listRoles", listRoles);
        return "user_form";
    }

    @PostMapping("/users/save")
    public String saveUser(User user) {
        userService.save(user);

        return "redirect:/users";
    }

    }
