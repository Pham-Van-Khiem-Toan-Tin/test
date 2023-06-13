package com.example.demo.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Test {
    @PostMapping("/login")
    public ResponseEntity<String> resLogin() {
        String successMessage = "Login successfully";
        return ResponseEntity.ok(successMessage);
    }
}
