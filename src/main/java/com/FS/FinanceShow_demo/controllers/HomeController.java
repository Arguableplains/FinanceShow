package com.FS.FinanceShow_demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class HomeController{

    public HomeController(){
    }

    @GetMapping("/hello")
    public String hello(){
        return "/hello";
    }

    @GetMapping("/new-transaction")
    public String new_transaction(){
        return "/new-transaction";
    }

}
