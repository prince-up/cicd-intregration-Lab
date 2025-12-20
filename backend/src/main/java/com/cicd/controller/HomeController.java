package com.cicd.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import java.util.HashMap;

@RestController
public class HomeController {

    @GetMapping("/")
    public Map<String, String> home() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "online");
        response.put("message", "CI/CD Pipeline Backend is Running 🚀");
        response.put("documentation", "/swagger-ui.html (if enabled)");
        return response;
    }
}
