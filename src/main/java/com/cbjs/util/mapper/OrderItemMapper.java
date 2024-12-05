package com.cbjs.util.mapper;

import org.mapstruct.Mapper;

import com.cbjs.dto.OrderItem;
import com.cbjs.entity.OrderItemEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    List<OrderItem> toDtos(List<OrderItemEntity> orderItemEntities);
    OrderItem toDto(OrderItemEntity orderItemEntity);

    List<OrderItemEntity> toEntities(List<OrderItem> orderItems);
    OrderItemEntity toEntity(OrderItem orderItem);
}
