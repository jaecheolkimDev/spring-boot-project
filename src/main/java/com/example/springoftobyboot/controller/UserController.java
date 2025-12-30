package com.example.springoftobyboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.example.springoftobyboot.service.UserService;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

}
