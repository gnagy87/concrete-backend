package com.concrete.poletime.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserControllerAPI {

    @GetMapping("/")
    public ResponseEntity helloApp() {
        return ResponseEntity.ok().body("Hello World!");
    }
}
