package com.FS.FinanceShow_demo.controllers;

import com.FS.FinanceShow_demo.security.CustomUserDetails;
import com.FS.FinanceShow_demo.entity.Transaction;
import com.FS.FinanceShow_demo.entity.User;
import com.FS.FinanceShow_demo.entity.Category;
import com.FS.FinanceShow_demo.entity.Account;
import com.FS.FinanceShow_demo.services.TransactionService;
import com.FS.FinanceShow_demo.services.CategoryService;
import com.FS.FinanceShow_demo.services.AccountService;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
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
    @Autowired
    private AccountService accountService;
    @Autowired
    private final HttpServletResponse response;

    public TransactionController(TransactionService transactionService, CategoryService categoryService, AccountService accountService, HttpServletResponse response) {
        this.transactionService = transactionService;
        this.categoryService = categoryService;
        this.accountService = accountService;
        this.response = response;
    }
    
    @GetMapping("/registration")
    public String showRegistrationForm(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails, HttpServletRequest request) {
        Transaction transaction = new Transaction();

        // Set base transaction account value¨
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("account_data".equals(cookie.getName()) && (String)(cookie.getValue()) != null ) {
                    transaction.setAccount(accountService.findById(Long.parseLong((String)(cookie.getValue()))));
                    break;
                }
            }
        }

        model.addAttribute("transaction", transaction);

        // Get User Categories
        List<Category> categories = categoryService.findByUserId(customUserDetails.getId());

        if(categories.isEmpty()){
            model.addAttribute("noCategoriesFound", "Você ainda não tem categorias registradas!!! Você deveria registrar categorias para que as suas transações fiquem melhor organizadas!");
        }

        // Get User's Accounts
        List<Account> accounts = accountService.findByUserId(customUserDetails.getId());

        // Passing Values to the view
        model.addAttribute("categories", categories);
        model.addAttribute("accounts", accounts);

        return "/transaction/registration";
    }
    
    // Save transaction
    @PostMapping("/save")
    public String saveNewTransaction(
            @ModelAttribute("transaction") @Valid Transaction transaction,
            @RequestParam("category") Long categoryId,
            @RequestParam("account") Long accountId,
            @RequestParam(value = "isIncome", required = false) Boolean isIncome,
            @RequestParam("description") String description,
            BindingResult bindingResult,
            Model model,
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            HttpServletRequest request) {
        
        // Validation For Invalid Amount
        if (transaction.getAmount() == 0) {
            List<Category> categories = categoryService.findByUserId(((User)customUserDetails.getUser()).getId());
            model.addAttribute("categories", categories);
            bindingResult.rejectValue("amount", "error.transaction", "Invalid Amount");
        }

        // Convert amount to negative if it's an expense
        if (isIncome == null || !isIncome) {
            transaction.setAmount(transaction.getAmount() * -1);
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "/transaction/registration";
        }

        try {
            // Use the authenticated user directly
            // Set User
            User authenticatedUser = customUserDetails.getUser();
            transaction.setUser(authenticatedUser);

            // Set Category
            Category category = categoryService.findById(categoryId);
            transaction.setCategory(category);

            // Set Account - via cookies
            String accountValue = null;

            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("account_data".equals(cookie.getName())) {
                        accountValue = cookie.getValue();
                        break;
                    }
                }
            }

            if (accountValue != null) {
                Account account = accountService.findById(Long.parseLong(accountValue));
                transaction.setAccount(account);
            } else {
                // Set Account - via view information
                transaction.setAccount(accountService.findById(accountId));
            }

            // Clean Cookie Account Info

            Cookie oldcookie = new Cookie("account_data", null);
            oldcookie.setPath("/");
            oldcookie.setMaxAge(0);
            response.addCookie(oldcookie);

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
        User currentUser = (User) customUserDetails.getUser();
        if (!transaction.getUser().getId().equals(currentUser.getId())) {
            return "redirect:hello";
        }
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        String formattedHappenedOn = transaction.getHappenedOn().format(formatter);
        List<Category> categories = categoryService.findByUserId(((User)customUserDetails.getUser()).getId());
        
        // Get User's Accounts
        List<Account> accounts = accountService.findByUserId(customUserDetails.getId());

        // Passing Values to the view
        model.addAttribute("accounts", accounts);

        if(categories.isEmpty()){
            model.addAttribute("noCategoriesFound", "Você ainda não tem categorias registradas!!! Você deveria registrar categorias para que as suas transações fiquem melhor organizadas!");
        }
        else{
            model.addAttribute("categories", categories);
        }

        model.addAttribute("transaction", transaction);
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

    // Update Accounts Cookie via JavaScript request
    @PostMapping("/update-account-cookie")
    public ResponseEntity<Map<String, Object>> updateTransactions(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody Map<String, String> requestBody) {

        // Get the account value from cookie
        String accountValue = requestBody.get("account_value");

        // Cookies - Update the active user account cookie

        // Clear Actual Account Cookie
        Cookie oldcookie = new Cookie("account_data", null);
        oldcookie.setPath("/");
        oldcookie.setMaxAge(0);
        response.addCookie(oldcookie);

        // Create new Account Cookie
        Cookie cookie = new Cookie("account_data", accountValue);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60);
        response.addCookie(cookie);

        // Return the updated data as JSON
        Map<String, Object> response = new HashMap<>();

        return ResponseEntity.ok(response);
    }
}
