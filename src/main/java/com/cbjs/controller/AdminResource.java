package com.cbjs.controller;

import java.util.List;
import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cbjs.dto.User;
import com.cbjs.service.AdminService;
import com.cbjs.service.UserService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/admin")
@Tag(name = "Admin", description = "Admin API")
public class AdminResource {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/generate-password")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Map<String, String>> generatePassword(HttpServletRequest request) {
        return adminService.generatePassword(request);
    }

    @GetMapping("/generate-config")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Map<String, String>> generateConfig(HttpServletRequest request) {
        return adminService.generateConfig(request);
    }

    @GetMapping("/secret")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Map<String, String>> getAdminSecret(HttpServletRequest request) {
        return adminService.getAdminSecret(request);
    }
}
