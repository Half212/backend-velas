package com.velassj.backend.api.controller;

import com.velassj.backend.api.dto.UserRequestDTO;
import com.velassj.backend.api.dto.UserResponseDTO;
import com.velassj.backend.domain.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.velassj.backend.api.dto.LoginRequestDTO;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserResponseDTO> getAllUsers() {
        return userService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO createUser(@RequestBody @Valid UserRequestDTO request) {
        return userService.create(request);
    }

    @PostMapping("/login")
    public UserResponseDTO login(@RequestBody @Valid LoginRequestDTO request) {
        return userService.authenticate(request.getEmail(), request.getPassword());
    }
}
