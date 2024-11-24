package com.FS.FinanceShow_demo.controllers;

import com.FS.FinanceShow_demo.CustomUserDetails;
import com.FS.FinanceShow_demo.entity.Category;
import com.FS.FinanceShow_demo.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import com.FS.FinanceShow_demo.services.UserService;
import com.FS.FinanceShow_demo.services.CustomUserDetailsService;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Controller
@RequestMapping("/user")
public class UserController {

  private final UserService userService;
  private final CustomUserDetailsService userDetailsService;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UserController(UserService userService, PasswordEncoder passwordEncoder, CustomUserDetailsService userDetailsService) {
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
    this.userDetailsService = userDetailsService;
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

  // Go to profile page
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

  // Delete own account
  @GetMapping("/delete")
  public String deleteUser(Model model,
    @AuthenticationPrincipal CustomUserDetails customUserDetails) {

    User user = (User) customUserDetails.getUser();
    userService.delete(user);

    SecurityContextHolder.clearContext();

    return "redirect:/user/login";
  }

  // Update user
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

    // New Authentication
    UserDetails updatedUserDetails = userDetailsService.loadUserByUsername(user.getEmail());

    UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(
        updatedUserDetails,
        updatedUserDetails.getPassword(),
        updatedUserDetails.getAuthorities()
    );

    SecurityContextHolder.getContext().setAuthentication(newAuth);

    redirectAttributes.addFlashAttribute("successMessage", true);
    return "redirect:/user/profile";
  }

  // List all users
    @GetMapping("/list")
    public String listUsers(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "/user/index";
    }

  // Delete user
  @GetMapping("/delete/{id}")
  public String deleteCategory(@PathVariable("id") Long id, Model model) {
      try {
          userService.deleteById(id);
          return "redirect:/user/list";
      } catch (Exception e) {
          model.addAttribute("deleteError", "An error occurred during deletion");
          return "redirect:/user/list";
      }
  }

}
