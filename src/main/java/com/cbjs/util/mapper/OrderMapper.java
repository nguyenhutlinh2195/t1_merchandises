package com.cbjs.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.cbjs.dto.Order;
import com.cbjs.entity.OrderEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "userId", source = "userEntity.id")
    List<Order> toDtos(List<OrderEntity> orderEntities);

    @Mapping(target = "userId", source = "userEntity.id")
    Order toDto(OrderEntity orderEntity);

    List<OrderEntity> toEntities(List<Order> orders);

    OrderEntity toEntity(Order order);
}
