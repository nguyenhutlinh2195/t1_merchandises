package com.cbjs.service;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cbjs.dto.AuthenticationRequest;
import com.cbjs.dto.AuthenticationResponse;
import com.cbjs.dto.RegisterRequest;
import com.cbjs.entity.RoleEnum;
import com.cbjs.entity.UserEntity;
import com.cbjs.repository.UserRepository;
import com.cbjs.util.exception.InputValidationException;
import com.cbjs.util.mapper.UserMapper;

import java.util.HashMap;

@Service
public class AuthenticationService {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${user.default-image}")
    private String defaultImage;

    public AuthenticationResponse register(RegisterRequest request){
        var user = UserEntity.builder()
                .email(request.getEmail())
                .name(request.getName())
                .role(RoleEnum.USER)
                .image(defaultImage)
                .password(request.getPassword())
                .balance(10.0)
                .build();
        userService.createUser(user);
        var jwtToken = jwtService.generateToken(null,user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .expiration(jwtService.extractClaim(jwtToken,Claims::getExpiration))
                .user(userMapper.toDto(user))
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request){
        validateUser(request);
        var user = userService.getUserByEmail(request.getEmail().trim());
        var jwtToken = jwtService.generateToken(new HashMap<>(), userMapper.toEntity(user));
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .expiration(jwtService.extractClaim(jwtToken, Claims::getExpiration))
                .user(user)
                .build();
    }

    private void validateUser(AuthenticationRequest request) {
        if(request.getEmail().isBlank()){
            throw new InputValidationException("Email is required");
        }
        if(request.getPassword().isBlank()){
            throw new InputValidationException("Password is required");
        }
        UserEntity userEntity = userRepository.findByEmail(request.getEmail().trim()).orElse(null);
        if(userEntity == null || !passwordEncoder.matches(request.getPassword(), userEntity.getPassword()))
            throw new InputValidationException("Email or password is incorrect");
    }
}
