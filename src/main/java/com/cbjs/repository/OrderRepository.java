package com.cbjs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cbjs.entity.OrderEntity;
import com.cbjs.entity.OrderStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {
    List<OrderEntity> findAllByUserEntityId(Long userId);

    Optional<OrderEntity> findByUserEntityIdAndStatus(Long userId, OrderStatus orderStatus);
}
