package com.cbjs.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cbjs.dto.Merch;
import com.cbjs.service.MerchService;

import java.util.List;

@RestController
@Tag(name = "Merch")
@RequestMapping("/v1/merchs")
public class MerchResource {
    @Autowired
    private MerchService merchService;

    @GetMapping
    @Operation(
            description = "Get all merchandises",
            summary = "Get all merchandises",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Get all merchandises successfully"),
                    @ApiResponse(responseCode = "404", description = "Merchandise not found"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized / Invalid Token")
            }
    )
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<List<Merch>> getAllMerchs() {
        return ResponseEntity.ok(merchService.getAllMerchs());
    }

    @GetMapping("/{id:\\d+}")
    @Operation(
            description = "Get merchandise by id",
            summary = "Get merchandise by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Get merchandise by id successfully"),
                    @ApiResponse(responseCode = "404", description = "Merchandise not found"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized / Invalid Token")
            }
    )
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Merch> getMerchById(@PathVariable("id") Long id){
        return ResponseEntity.ok(merchService.getMerchById(id));
    }
}
