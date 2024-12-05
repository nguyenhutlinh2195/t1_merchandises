package com.cbjs.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.cbjs.dto.AddOrderItemRequest;
import com.cbjs.dto.Order;
import com.cbjs.dto.OrderItem;
import com.cbjs.service.OrderItemService;
import com.cbjs.service.OrderService;
import com.cbjs.util.exception.InputValidationException;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@Tag(name = "Order")
@RequestMapping("/v1/orders")
public class OrderResource {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;

    @GetMapping
    @Operation(
            description = "Get all orders",
            summary = "Get all orders",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Get all orders successfully")
            }
    )
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<List<Order>> getAllOrders(){
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Order> getOrderById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping("/history")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<List<Order>> getOrdersHistory(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(orderService.getOrdersHistory(authentication));
    }

    @GetMapping("/details/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<List<OrderItem>> getOrderDetailsById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(orderService.getOrderDetailsById(id));
    }

    @PostMapping("/create")
    @Operation(
            description = "Add order items",
            summary = "Add order items to create new order",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request / Not enough stock / Insufficient balance"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized")
            }
    )
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Map<String, Double>> addItems(@Valid @RequestBody List<AddOrderItemRequest> request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            return ResponseEntity.ok(Map.of("balance", orderItemService.addOrderItem(request, authentication)));
        } catch (InputValidationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
