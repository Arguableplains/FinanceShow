package com.FS.FinanceShow_demo.controllers;

import com.FS.FinanceShow_demo.security.CustomUserDetails;
import com.FS.FinanceShow_demo.entity.Category;
import com.FS.FinanceShow_demo.entity.User;
import com.FS.FinanceShow_demo.entity.Role;
import org.springframework.security.core.userdetails.UserDetails;
import com.FS.FinanceShow_demo.services.UserService;
import com.FS.FinanceShow_demo.services.CustomUserDetailsService;
import com.FS.FinanceShow_demo.services.CategoryService;
import com.FS.FinanceShow_demo.services.RoleService;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

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
  private final CategoryService categoryService;
  private final RoleService roleService;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UserController(UserService userService, PasswordEncoder passwordEncoder, CustomUserDetailsService userDetailsService, CategoryService categoryService, RoleService roleService) {
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
    this.userDetailsService = userDetailsService;
    this.categoryService = categoryService;
    this.roleService = roleService;
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
      @Valid @ModelAttribute("user") User user,
      BindingResult bindingResult,
      Model model) {

    if (bindingResult.hasErrors()) {
      return "/user/registration";
    }

    try {
      user.setPassword(passwordEncoder.encode(user.getPassword()));

      // Basic Role
      Set<Role> roles = new HashSet<>();
      roles.add(roleService.findByName("ROLE_USER"));
      user.setRoles(roles);

      userService.save(user);

      // Basic Categories For user
      Category[] defaultCategories = {
          new Category("Food/Drinks", user),
          new Category("Entertainment", user),
          new Category("Health", user)
      };

      // Save categories
      for (Category category : defaultCategories) {
          categoryService.save(category);
      }

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
  public String showUpdateUserForm(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

    User user = userService.findById(customUserDetails.getId());

    model.addAttribute("user", user);
    model.addAttribute("allRoles", roleService.findAll());
    return "/user/profile";
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
      RedirectAttributes redirectAttributes,
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {

    if (result.hasErrors()) {
      user.setId(id);
      return "/user/profile";
    }

    User existingUser = userService.findById(id);

    if (!user.getPassword().equals(existingUser.getPassword())) {
      user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

    userService.save(user);

    User currentUser = (User) customUserDetails.getUser();

    if (user.getId().equals(currentUser.getId())) {
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
    } else {
      model.addAttribute("user", user);
      model.addAttribute("allRoles", roleService.findAll());
      return "user/profile";
    }

  }

  // List all users
  @GetMapping("/list")
  public String listUsers(Model model) {
      List<User> users = userService.findAll();
      model.addAttribute("users", users);
      return "/user/index";
  }

  // Edit user
  @GetMapping("/edit/{id}")
  public String showEditForm(
      @PathVariable("id") Long id, 
      Model model) {
      User user = userService.findById(id);
      if (user == null) {
          model.addAttribute("error", "User not found");
          return "redirect:/hello";
      }

      model.addAttribute("user", user);
      model.addAttribute("allRoles", roleService.findAll());
      return "user/profile";
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
