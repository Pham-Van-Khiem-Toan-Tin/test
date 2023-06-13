package com.example.demo.Controller;

import com.example.demo.Config.JWTuntil;
import com.example.demo.Model.Employee;
import com.example.demo.Services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    private EmployeeService employeeService;
    @GetMapping("/login")
    public String login(Authentication authentication) {

        return "login";
    }
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new Employee());
        return "register";
    }
    @GetMapping("/admin")
    public  String admin() {
        return "admin";
    }
    @GetMapping("/user")
    public String user() {
        return "user";
    }


    @PostMapping("/register")
    public String processRegistration(@ModelAttribute("user") Employee employee) {
        employeeService.register(employee);
        return "redirect:/login";
    }
}
