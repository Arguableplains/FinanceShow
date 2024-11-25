package com.FS.FinanceShow_demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.http.ResponseEntity;

import com.FS.FinanceShow_demo.security.CustomUserDetails;

import com.FS.FinanceShow_demo.services.AccountService;
import com.FS.FinanceShow_demo.entity.Account;

import com.FS.FinanceShow_demo.services.TransactionService;
import com.FS.FinanceShow_demo.entity.Transaction;

import com.FS.FinanceShow_demo.services.UserService;
import com.FS.FinanceShow_demo.entity.User;

import com.FS.FinanceShow_demo.services.CategoryService;
import com.FS.FinanceShow_demo.entity.Category;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/")
public class HomeController{
    
    private final TransactionService transactionService;
    private final AccountService accountService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final HttpServletResponse response;

    public HomeController(TransactionService transactionService, AccountService accountService, UserService userService, CategoryService categoryService, HttpServletResponse response) {
        this.transactionService = transactionService;
        this.accountService = accountService;
        this.userService = userService;
        this.response = response;
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public String home(){
        return "/free-pages/home";
    }

    @GetMapping("/hello")
    public String hello(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        List<Transaction> transactions = transactionService.findAllTransactionsForCurrentUser();
        List<Account> accounts = accountService.findByUserId(customUserDetails.getId());
        List<Category> categories = categoryService.findByUserId(customUserDetails.getId());
        User currentUser = userService.findById(customUserDetails.getId());
        model.addAttribute("transactions", transactions);
        model.addAttribute("categories", categories);
        model.addAttribute("accounts", accounts);
        model.addAttribute("user", currentUser);
        
        return "/hello";
    }

    // Fetch transactions to the Javascript request
    @PostMapping("/update-transactions")
    public ResponseEntity<Map<String, Object>> updateTransactions(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody Map<String, String> requestBody) {

        // Get the new account and category value to be filtered
        String accountValue = requestBody.get("account_value");
        String categoryValue = requestBody.get("category_value");

        // Cookies - Set the active account to filter transactions

        // Clear Account Cookie
        Cookie oldcookie = new Cookie("account_data", null);
        oldcookie.setPath("/");
        oldcookie.setMaxAge(0);
        response.addCookie(oldcookie);

        // Create Account Cookie
        Cookie cookie = new Cookie("account_data", accountValue);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60);
        response.addCookie(cookie);

        // Get Transactions
        List<Transaction> transactions = null;
        if(accountValue.equals("0") & categoryValue.equals("0")){
            transactions = transactionService.findAllTransactionsForCurrentUser();
        }
        else if(accountValue.equals("0") & !categoryValue.equals("0")){
            transactions = transactionService.findAllByUserIdAndCategoryId(customUserDetails.getId(), Long.valueOf(categoryValue));
        }
        else if(!accountValue.equals("0") & categoryValue.equals("0")){
            transactions = transactionService.findAllByUserIdAndAccountId(customUserDetails.getId(), Long.valueOf(accountValue));
        }
        else{
            transactions = transactionService.findAllByUserIdAndAccountIdAndCategoryId(customUserDetails.getId(), Long.valueOf(accountValue), Long.valueOf(categoryValue));
        }

        // Return the updated data as JSON
        Map<String, Object> response = new HashMap<>();
        response.put("transactions", transactions);

        return ResponseEntity.ok(response);
    }
}

