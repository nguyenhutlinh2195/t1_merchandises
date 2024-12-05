package com.cbjs.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.cbjs.dto.Order;
import com.cbjs.dto.OrderItem;
import com.cbjs.entity.OrderEntity;
import com.cbjs.entity.OrderStatus;
import com.cbjs.entity.UserEntity;
import com.cbjs.repository.OrderItemRepository;
import com.cbjs.repository.OrderRepository;
import com.cbjs.util.mapper.OrderItemMapper;
import com.cbjs.util.mapper.OrderMapper;

import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    public List<Order> getAllOrders() {
        return orderMapper.toDtos(orderRepository.findAll());
    }

    public Order getOrderById(UUID id) {
        OrderEntity orderEntity = orderRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("No order found"));
        return orderMapper.toDto(orderEntity);
    }

    public OrderEntity getOrderByUserIdAndStatus(Long userId, OrderStatus orderStatus) {
        return orderRepository.findByUserEntityIdAndStatus(userId, orderStatus)
            .orElseThrow(() -> new EntityNotFoundException("No order found"));
    }

    public Order save(Order order){
        return orderMapper.toDto(orderRepository.save(orderMapper.toEntity(order)));
    }

    public List<Order> getOrdersHistory(Authentication authentication) {
        UserEntity userEntity = (UserEntity) userDetailsService.loadUserByUsername(authentication.getName());
        List<OrderEntity> orderEntities = orderRepository.findAllByUserEntityId(userEntity.getId());
        return orderMapper.toDtos(orderEntities);
    }

    public List<OrderItem> getOrderDetailsById(UUID id) {
        if (!orderRepository.existsById(id)) {
            throw new EntityNotFoundException("No order item found");
        }
        return orderItemMapper.toDtos(orderItemRepository.findAllByOrderEntityId(id));
    }   
}
