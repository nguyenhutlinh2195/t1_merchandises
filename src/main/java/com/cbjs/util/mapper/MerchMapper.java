package com.cbjs.util.mapper;

import org.mapstruct.Mapper;

import com.cbjs.dto.Merch;
import com.cbjs.entity.MerchEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MerchMapper {
    Merch toDto(MerchEntity merchEntity);
    MerchEntity toEntity(Merch merch);
    
    List<Merch> toDtos(List<MerchEntity> merchEntities);
    List<MerchEntity> toEntities(List<Merch> merchs);
}
