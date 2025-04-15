package com.poker.controller;

import com.poker.model.User;
import com.poker.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private IUserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        return ResponseEntity.ok("login success");
        // User user = userService.authenticate(username, password);
        // if (user != null) {
        //     Map<String, Object> response = new HashMap<>();
        //     response.put("token", "temp-token-" + username); // 临时token
        //     response.put("username", username);
        //     return ResponseEntity.ok(response);
        // }

        // return ResponseEntity.badRequest().body("Invalid credentials");
    }
} 