package com.cbjs.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cbjs.dto.Merch;
import com.cbjs.repository.MerchRepository;
import com.cbjs.util.mapper.MerchMapper;

import java.util.List;

@Service
public class MerchService {
    @Autowired
    private MerchMapper merchMapper;

    @Autowired
    private MerchRepository merchRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public List<Merch> getAllMerchs() {
        return merchMapper.toDtos(merchRepository.findAll());
    }

    public Merch getMerchById(Long id) {
        return merchMapper.toDto(merchRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Merch not found")));
    }
}
