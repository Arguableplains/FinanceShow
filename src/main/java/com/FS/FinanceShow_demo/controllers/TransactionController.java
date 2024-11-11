package com.FS.FinanceShow_demo.controllers;

import com.FS.FinanceShow_demo.CustomUserDetails;
import com.FS.FinanceShow_demo.entity.Transaction;
import com.FS.FinanceShow_demo.entity.User;
import com.FS.FinanceShow_demo.entity.Category;
import com.FS.FinanceShow_demo.services.TransactionService;
import com.FS.FinanceShow_demo.services.CategoryService;
import jakarta.validation.Valid;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/transaction")
public class TransactionController {
    
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private CategoryService categoryService;
    
    @GetMapping("/registration")
    public String showRegistrationForm(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Transaction transaction = new Transaction();
        model.addAttribute("transaction", transaction);

        // Get User Categories
        User user = customUserDetails.getUser();
        List<Category> categories = categoryService.findByUserId(user.getId());

        if(categories.isEmpty()){
            model.addAttribute("noCategoriesFound", "You don't have any categories registred yet! You should create one before creating transactions!");
        }

        model.addAttribute("categories", categories);

        return "/transaction/registration";
    }
    
    // Save transaction
    @PostMapping("/save")
    public String saveNewTransaction(
            @ModelAttribute("transaction") @Valid Transaction transaction,
            @RequestParam("category") Long categoryId,
            BindingResult bindingResult,
            Model model,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        
        if (transaction.getAmount() == 0) {
            List<Category> categories = categoryService.findByUserId(((User)customUserDetails.getUser()).getId());
            model.addAttribute("categories", categories);
            bindingResult.rejectValue("amount", "error.transaction", "Invalid Amount");
        }
        if (bindingResult.hasErrors()) {
            return "/transaction/registration";
        }

        try {
            // Use the authenticated user directly
            User authenticatedUser = customUserDetails.getUser();
            transaction.setUser(authenticatedUser);

            Category category = categoryService.findById(categoryId);
            transaction.setCategory(category);

            transactionService.save(transaction);
            return "redirect:/hello";
        } catch (Exception e) {
            model.addAttribute("registrationError", e.getMessage());
            return "/transaction/registration";
        }
    }
    
    // Edit transaction
    @GetMapping("/edit/{id}")
    public String showEditForm(
        @PathVariable("id") Long id, 
        Model model,
        @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Transaction transaction = transactionService.findById(id);
        if (transaction == null) {
            model.addAttribute("error", "Transaction not found");
            return "redirect:/hello";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        String formattedHappenedOn = transaction.getHappenedOn().format(formatter);
        List<Category> categories = categoryService.findByUserId(((User)customUserDetails.getUser()).getId());

        model.addAttribute("transaction", transaction);
        model.addAttribute("categories", categories);
        model.addAttribute("formattedHappenedOn", formattedHappenedOn);
        return "/transaction/edit";
    }
    
    // Update transaction
    @PostMapping("/update/{id}")
    public String updateTransaction(
            @PathVariable("id") Long id,
            @ModelAttribute("transaction") @Valid Transaction transaction,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "/transaction/edit";
        }
        
        try {
            transactionService.save(transaction);
            return "redirect:/hello";
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred during update");
            System.out.println(e.getMessage());
            return "/transaction/edit";
        }
    }
    
    // Delete transaction
    @GetMapping("/delete/{id}")
    public String deleteTransaction(
        @PathVariable("id") Long id, 
        Model model) {
        try {
            transactionService.deleteById(id);
            return "redirect:/hello";
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred during deletion");
            return "redirect:/hello";
        }
    }
}
