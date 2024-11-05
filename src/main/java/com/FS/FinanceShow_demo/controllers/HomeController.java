package com.FS.FinanceShow_demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import com.FS.FinanceShow_demo.services.TransactionService;
import com.FS.FinanceShow_demo.entity.Transaction;

import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController{
    
    private final TransactionService transactionService;

    public HomeController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/")
    public String home(){
        return "/free-pages/home";
    }

    @GetMapping("/hello")
    public String hello(Model model){
        List<Transaction> transactions = transactionService.findAllTransactionsForCurrentUser();
        model.addAttribute("transactions", transactions);
        
        return "/hello";
    }

}
