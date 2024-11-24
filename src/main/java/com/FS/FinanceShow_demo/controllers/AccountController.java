package com.FS.FinanceShow_demo.controllers;

import com.FS.FinanceShow_demo.entity.Account;
import com.FS.FinanceShow_demo.services.AccountService;
import com.FS.FinanceShow_demo.entity.User;
import com.FS.FinanceShow_demo.services.UserService;
import com.FS.FinanceShow_demo.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private UserService userService;

    // Show Account registration form
    @GetMapping("/registration")
    public String showRegistrationForm(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        User user = userService.findByEmail(customUserDetails.getUsername());

        Account account = new Account();
        account.setUser(user);

        model.addAttribute("account", account);
        return "/account/registration";
    }

    // Save new Account
    @PostMapping("/save")
    public String saveNewAccount(
            @ModelAttribute("account") @Valid Account account,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "/account/registration";
        }

        try
            {
            accountService.save(account);
            return "redirect:/account/list";
        } catch (Exception e) {
            model.addAttribute("registrationError", e.getMessage());
            return "/account/registration";
        }
    }

    // List all accounts
    @GetMapping("/list")
    public String listCategories(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        User user = userService.findByEmail(customUserDetails.getUsername());

        List<Account> accounts = accountService.findByUserId(user.getId());
        model.addAttribute("accounts", accounts);
        return "/account/account-index";
    }

    // Show Account edit form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Account account = accountService.findById(id);
        if (account == null) {
            model.addAttribute("error", "account not found");
            return "redirect:/account/list";
        }
        model.addAttribute("account", account);
        return "/account/edit";
    }

    // Update Account
    @PostMapping("/update/{id}")
    public String updateAccount(
            @PathVariable("id") Long id,
            @ModelAttribute("account") @Valid Account account,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "/account/edit";
        }

        try {
            accountService.save(account);
            return "redirect:/account/list";
        } catch (Exception e) {
            model.addAttribute("updateError", "An error occurred during update");
            return "/account/edit";
        }
    }

    // Delete Account
    @GetMapping("/delete/{id}")
    public String deleteAccount(@PathVariable("id") Long id, Model model) {
        try {
            accountService.deleteById(id);
            return "redirect:/account/list";
        } catch (Exception e) {
            model.addAttribute("deleteError", "An error occurred during deletion");
            return "redirect:/account/list";
        }
    }
}
