package com.FS.FinanceShow_demo.controllers;

import com.FS.FinanceShow_demo.CustomUserDetails;
import com.FS.FinanceShow_demo.entity.User;
import com.FS.FinanceShow_demo.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.crypto.password.PasswordEncoder;

@Controller
@RequestMapping("/user")
public class UserController {

  private final UserService userService;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UserController(UserService userService, PasswordEncoder passwordEncoder) {
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
  }

  @GetMapping("/login")
  public String showLoginForm(Model model) {
    return "/user/login";
  }

  // Creating User
  @GetMapping("/registration")
  public String showRegistrationForm(Model model) {
    model.addAttribute("user", new User());
    return "/user/registration";
  }

  @PostMapping("/registration")
  public String saveNewUser(
      @ModelAttribute("user") @Valid User user,
      BindingResult bindingResult,
      Model model) {

    if (bindingResult.hasErrors()) {
      return "/user/registration";
    }

    try {
      user.setPassword(passwordEncoder.encode(user.getPassword()));
      userService.save(user);
      // return "redirect:/user/success";
      return "/user/login";
    } catch (Exception e) {
      model.addAttribute("registrationError", "An error occurred during registration");
      return "/user/registration";
    }
  }

  @GetMapping("/success")
  public String registrationSuccess() {
    return "/user/success";
  }

  // Updating User
  @GetMapping("/profile")
  public String showUpdateUserForm(Model model) {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    User user;
    if (principal instanceof CustomUserDetails customUserDetails) {
      user = customUserDetails.getUser();
    } else {
      return "redirect:/login";
    }

    model.addAttribute("user", user);
    return "user/profile";
  }

  // Deleting User
  @GetMapping("/delete/{id}")
  public String deleteUser(@PathVariable("id") long id, Model model) {
    User user = userService.findById(id);
    userService.delete(user);
    return "redirect:/home";
  }

  @PostMapping("/update/{id}")
  public String updateUser(
      @PathVariable("id") long id,
      @Valid User user,
      BindingResult result,
      Model model,
      RedirectAttributes redirectAttributes) {

    if (result.hasErrors()) {
      user.setId(id);
      return "/user/profile";
    }

    User existingUser = userService.findById(id);

    if (!user.getPassword().equals(existingUser.getPassword())) {
      user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

    userService.save(user);

    redirectAttributes.addFlashAttribute("successMessage", true);
    return "redirect:/user/profile";
  }
}
