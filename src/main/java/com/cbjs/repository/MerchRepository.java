package com.cbjs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cbjs.entity.MerchEntity;

import java.util.List;

@Repository
public interface MerchRepository extends JpaRepository<MerchEntity, Long> {
    boolean existsByName(String name);

    List<MerchEntity> findAll();

    MerchEntity findByName(String trim);
}
