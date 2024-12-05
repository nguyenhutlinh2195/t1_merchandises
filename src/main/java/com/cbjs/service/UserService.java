package com.cbjs.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cbjs.dto.UpdateProfileRequest;
import com.cbjs.dto.User;
import com.cbjs.entity.UserEntity;
import com.cbjs.repository.UserRepository;
import com.cbjs.util.exception.InputValidationException;
import com.cbjs.util.mapper.UserMapper;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EntityManager entityManager;

    public List<User> getAllUsers() {
        return userMapper.toDtos(userRepository.findAll());
    }

    public User getUserById(Long id) {
        return userMapper.toDto(userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found")));
    }

    public User getUserByEmail(String email) {
        return userMapper.toDto(userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User not found")));
    }

    public User createUser(UserEntity user) {
        validateUser(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        UserEntity createdUser = userRepository.save(user);
        return userMapper.toDto(createdUser);
    }

    public Optional<String> getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Optional.of(authentication.getName());
    }

    private void validateUser(UserEntity user) {
        if(user.getEmail() == null || user.getEmail().isBlank()){
            throw new InputValidationException("Email cannot be empty");
        }
        if(user.getName() == null || user.getName().isBlank()){
            throw new InputValidationException("Name cannot be empty");
        }
        if (userRepository.existsByEmail(user.getEmail().trim())) {
            throw new InputValidationException("Email already exists");
        }
        if(user.getPassword() == null || user.getPassword().isBlank()){
            throw new InputValidationException("Password cannot be empty");
        }
    }

    public User getProfile(Authentication authentication) {
        UserEntity userEntity = (UserEntity) userDetailsService.loadUserByUsername(authentication.getName());
        return userMapper.toDto(userEntity);
    }

    @Transactional
    public User updateProfile(Authentication authentication, UpdateProfileRequest updateProfileRequest) {
        UserEntity currentUser = (UserEntity) userDetailsService.loadUserByUsername(authentication.getName());
        String name = updateProfileRequest.getName();
        
        String dangerous = "(?i)(DELETE|DROP|UNION|EMAIL|BALANCE|ROLE|PASSWORD|TRUNCATE|AND|TRUE|FALSE)";
        String regex = "(?i)(SELECT|UPDATE|AND|NOT|#|;|\\\"| |<|>)";

        if ((name != null && name.matches(".*" + dangerous + ".*"))) {
            throw new InputValidationException("Invalid characters in name");
        }
        updateProfileRequest.setName(name != null ? name.replaceAll(regex, "") : null);

        // query
        String query = String.format("UPDATE users SET name = '%s' WHERE id = %d", 
            updateProfileRequest.getName(), 
            currentUser.getId());
            
        try {
            entityManager
                .createNativeQuery(query)
                .executeUpdate();
                
            // Refresh the user entity from database to get updated values
            entityManager.refresh(currentUser);
            
            // Return updated user
            return userMapper.toDto(currentUser);
            
        } catch (Exception e) {
            throw new InputValidationException("Failed to update profile");
        }
    }
}
