package com.cbjs.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cbjs.dto.UpdateProfileRequest;
import com.cbjs.dto.User;
import com.cbjs.service.UserService;

@RestController
@Tag(name = "Profile")
@RequestMapping("/v1/profile")
public class ProfileResource {

    @Autowired
    private UserService userService;

    @GetMapping
    @Operation(
            description = "Get user profile",
            summary = "Get user information",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Get user information successfully")
            }
    )
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<User> getProfile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(userService.getProfile(authentication));
    }

    @PostMapping
    @Operation(
            description = "Update user profile",
            summary = "Update user information",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized / Invalid Token")
            }
    )
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<User> updateProfile(@Valid @RequestBody UpdateProfileRequest updateProfileRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(userService.updateProfile(authentication, updateProfileRequest));
    }
}
