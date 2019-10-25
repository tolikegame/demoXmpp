package com.example.demo.controller;

import com.example.demo.service.impl.OfuserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Broadcast {

    @Autowired
    OfuserServiceImpl ofUserService;

    @PostMapping("/broadcast")
    public boolean Broadcast(){
        return ofUserService.Broadcast();
    }

}
