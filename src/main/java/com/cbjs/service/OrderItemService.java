package com.cbjs.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cbjs.dto.AddOrderItemRequest;
import com.cbjs.entity.MerchEntity;
import com.cbjs.entity.OrderEntity;
import com.cbjs.entity.OrderItemEntity;
import com.cbjs.entity.OrderStatus;
import com.cbjs.entity.UserEntity;
import com.cbjs.repository.OrderItemRepository;
import com.cbjs.repository.OrderRepository;
import com.cbjs.repository.UserRepository;
import com.cbjs.util.exception.InputValidationException;
import com.cbjs.util.mapper.MerchMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderItemService {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MerchService merchService;

    @Autowired
    private MerchMapper merchMapper;

    public List<OrderItemEntity> getOrderItems(){
        return orderItemRepository.findAll();
    }

    public OrderItemEntity getOrderItemById(Long id){
        return orderItemRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Order item not found"));
    }

    @Transactional
    public Double addOrderItem(List<AddOrderItemRequest> orderItemRequest, Authentication authentication) {
        UserEntity userEntity = (UserEntity) userDetailsService.loadUserByUsername(authentication.getName());
        double totalAmount = 0.0;
        
        // Calculate total amount first
        for(AddOrderItemRequest item : orderItemRequest) {
            MerchEntity merch = merchMapper.toEntity(merchService.getMerchById(item.getMerchId()));
            totalAmount += merch.getPrice() * item.getCount();
        }

        // Check user balance
        if (userEntity.getBalance() < totalAmount) {
            throw new InputValidationException("Insufficient balance. Required: $" + totalAmount + ", Available: $" + userEntity.getBalance());
        }

        // Create new order
        OrderEntity orderEntity = OrderEntity.builder()
                .userEntity(userEntity)
                .date(LocalDate.now())
                .status(OrderStatus.PLACED)
                .orderItemEntities(new ArrayList<>())
                .totalAmount(totalAmount)
                .build();

        // Save initial order
        orderEntity = orderRepository.save(orderEntity);
        List<OrderItemEntity> orderItems = new ArrayList<>();

        // Process each order item
        for(AddOrderItemRequest item : orderItemRequest) {
            MerchEntity merch = merchMapper.toEntity(merchService.getMerchById(item.getMerchId()));
            double itemTotal = merch.getPrice() * item.getCount();

            OrderItemEntity orderItem = OrderItemEntity.builder()
                    .count(item.getCount())
                    .orderEntity(orderEntity)
                    .address("Unknown address")
                    .merchId(merch.getId())
                    .merchImage(merch.getImagePath())
                    .merchName(merch.getName())
                    .total(itemTotal)
                    .build();

            orderItems.add(orderItemRepository.save(orderItem));
        }

        // Update user balance
        userEntity.setBalance(userEntity.getBalance() - totalAmount);
        userRepository.save(userEntity);

        // Update order with items
        orderEntity.setOrderItemEntities(orderItems);
        orderRepository.save(orderEntity);

        return userEntity.getBalance();
    }
}
