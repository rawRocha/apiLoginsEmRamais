package com.project.projectLoginInExtensionsApi.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.projectLoginInExtensionsApi.dto.UserDTO;
import com.project.projectLoginInExtensionsApi.model.User;
import com.project.projectLoginInExtensionsApi.repository.UserRepository;
import com.project.projectLoginInExtensionsApi.service.PasswordService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    private final PasswordService passwordService;

    public UserController(UserRepository userRepository, PasswordService passwordService) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
    }

    @GetMapping
    public List<User> listarUsuarios() {
        return userRepository.findAll();
    }

    @PostMapping("/register")
    public ResponseEntity<?> criarUsuario(@Valid @RequestBody UserDTO dto) {
        String hashedPassword = passwordService.hashPassword(dto.getPassword());
        dto.setPassword(hashedPassword);

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());

        Optional<User> existingUser = userRepository
                .findByUsername(user.getUsername());

        if (existingUser.isPresent()) {
            throw new IllegalStateException("Nome de usuário já existe.");
        }

        User newUser = userRepository.save(user);
        return ResponseEntity.ok(newUser);

    }
}
