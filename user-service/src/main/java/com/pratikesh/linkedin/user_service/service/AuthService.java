package com.pratikesh.linkedin.user_service.service;

import com.pratikesh.linkedin.user_service.dto.LoginRequestDto;
import com.pratikesh.linkedin.user_service.dto.SignUpRequestDto;
import com.pratikesh.linkedin.user_service.dto.UserDto;
import com.pratikesh.linkedin.user_service.entity.User;
import com.pratikesh.linkedin.user_service.exception.BadRequestException;
import com.pratikesh.linkedin.user_service.repository.UserRepo;
import com.pratikesh.linkedin.user_service.utils.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final ModelMapper modelMapper;
    private final UserRepo userRepo;
    private final JwtService jwtService;
    public UserDto signUp(SignUpRequestDto signUpRequestDto) {

        boolean userExists = userRepo.existsByEmail(signUpRequestDto.getEmail());
        if (userExists){
            throw new BadRequestException("User already exists, cannot signup again!");
        }

        User user = modelMapper.map(signUpRequestDto, User.class);
        user.setPassword(PasswordUtil.hashPassword(signUpRequestDto.getPassword()));

        User savedUser = userRepo.save(user);
        return modelMapper.map(savedUser, UserDto.class);
    }

    public String logIn(LoginRequestDto logInRequestDto) {
        User user = userRepo.findByEmail(logInRequestDto.getEmail())
                .orElseThrow(()-> new RuntimeException("User not found with email id : "+logInRequestDto.getEmail()));

        boolean isPasswordMatch = PasswordUtil.checkPassword(logInRequestDto.getPassword(), user.getPassword());
        if(!isPasswordMatch){
            throw new BadRequestException("Incorrect password");
        }
        return jwtService.generateAccessToken(user);
    }
}
