package com.FS.FinanceShow_demo.controllers;

import com.FS.FinanceShow_demo.entity.Category;
import com.FS.FinanceShow_demo.services.CategoryService;
import com.FS.FinanceShow_demo.entity.User;
import com.FS.FinanceShow_demo.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;

    // Show category registration form
    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userService.findByEmail(email);

        Category category = new Category();
        category.setUser(user);

        model.addAttribute("category", category);
        return "/category/registration";
    }

    // Save new category
    @PostMapping("/save")
    public String saveNewCategory(
            @ModelAttribute("category") @Valid Category category,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "/category/registration";
        }

        try
            {
            categoryService.save(category);
            return "redirect:/category/list";
        } catch (Exception e) {
            model.addAttribute("registrationError", e.getMessage());
            return "/category/registration";
        }
    }

    // List all categories
    @GetMapping("/list")
    public String listCategories(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userService.findByEmail(email);

        List<Category> categories = categoryService.findByUserId(user.getId());
        model.addAttribute("categories", categories);
        return "/category/category-index";
    }

    // Show category edit form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Category category = categoryService.findById(id);
        if (category == null) {
            model.addAttribute("error", "Category not found");
            return "redirect:/category/list";
        }
        model.addAttribute("category", category);
        return "/category/edit";
    }

    // Update category
    @PostMapping("/update/{id}")
    public String updateCategory(
            @PathVariable("id") Long id,
            @ModelAttribute("category") @Valid Category category,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "/category/edit";
        }

        try {
            categoryService.save(category);
            return "redirect:/category/list";
        } catch (Exception e) {
            model.addAttribute("updateError", "An error occurred during update");
            return "/category/edit";
        }
    }

    // Delete category
    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id, Model model) {
        try {
            categoryService.deleteById(id);
            return "redirect:/category/list";
        } catch (Exception e) {
            model.addAttribute("deleteError", "An error occurred during deletion");
            return "redirect:/category/list";
        }
    }
}
