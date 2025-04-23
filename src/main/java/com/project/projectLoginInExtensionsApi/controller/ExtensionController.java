package com.project.projectLoginInExtensionsApi.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.projectLoginInExtensionsApi.dto.ExtensionLoginRequest;
import com.project.projectLoginInExtensionsApi.dto.ExtensionRangeDTO;
import com.project.projectLoginInExtensionsApi.enums.StatusExtension;
import com.project.projectLoginInExtensionsApi.model.Extension;
import com.project.projectLoginInExtensionsApi.model.User;
import com.project.projectLoginInExtensionsApi.repository.ExtensionRepository;
import com.project.projectLoginInExtensionsApi.repository.UserRepository;
import com.project.projectLoginInExtensionsApi.service.ExtensionService;
import com.project.projectLoginInExtensionsApi.service.PasswordService;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/extensions")
public class ExtensionController {
    private final ExtensionRepository extensionRepository;

    private final UserRepository userRepository;

    private final PasswordService passwordService;

    private final ExtensionService extensionService;

    public ExtensionController(UserRepository userRepository, PasswordService passwordService,
            ExtensionRepository extensionRepository, ExtensionService extensionService) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
        this.extensionRepository = extensionRepository;
        this.extensionService = extensionService;
    }

    @PostMapping("/create-range")
    public ResponseEntity<List<Extension>> createRange(@RequestBody ExtensionRangeDTO dto) {
        List<Extension> extensoesCriadas = extensionService.createRangeExtensions(dto.getStart(), dto.getEnd());
        return ResponseEntity.ok(extensoesCriadas);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createExtension(@RequestBody Extension extension) {
        if (extension.getExtensionNumber() == null) {
            throw new IllegalArgumentException("O número de ramal é obrigatório.");
        }

        Optional<Extension> existingExtension = extensionRepository
                .findByExtensionNumber(extension.getExtensionNumber());

        if (existingExtension.isPresent()) {
            throw new IllegalArgumentException("Número de ramal já cadastrado!");
        }

        // Verifica se o usuário logado existe, se foi passado
        if (extension.getLoggedUser() != null) {
            Optional<User> userOptional = userRepository.findById(extension.getLoggedUser().getId());
            if (userOptional.isPresent()) {
                extension.setLoggedUser(userOptional.get());
            } else {
                throw new IllegalArgumentException("Usuário informado não existe.");
            }
        }

        Extension savedExtension = extensionRepository.save(extension);
        return ResponseEntity.status(201).body(savedExtension);
    }

    @GetMapping("/available")
    public ResponseEntity<Page<Extension>> getAvailableExtensions(@RequestParam(required = false) Integer start,
            @RequestParam(required = false) Integer end,
            @PageableDefault(size = 8) Pageable pageable) {

        Page<Extension> availableExtensions;

        if (start != null && end != null) {
            availableExtensions = extensionRepository.findByLoggedUserIsNullAndExtensionNumberBetween(start, end,
                    pageable);
        } else {
            availableExtensions = extensionRepository.findByStatus(StatusExtension.DISPONIVEL, pageable);
        }

        return ResponseEntity.ok(availableExtensions);
    }

    @GetMapping("/unvailable")
    public ResponseEntity<List<Extension>> getUnavailableExtensions() {
        List<Extension> unavailableExtensions = extensionRepository.findByLoggedUserIsNotNull();
        return ResponseEntity.ok(unavailableExtensions);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUserInExtension(@RequestBody ExtensionLoginRequest request) {
        Optional<User> optionalUser = userRepository.findByUsername(request.getUsername());
        if (optionalUser.isEmpty()) {
            throw new NoSuchElementException("Usuário não encontrado.");
        }

        User user = optionalUser.get();

        boolean senhaCorreta = passwordService.verifyPassword(request.getPassword(), user.getPassword());

        if (!senhaCorreta) {
            throw new SecurityException("Senha incorreta.");
        }

        Optional<Extension> optionalExtension = extensionRepository.findByExtensionNumber(request.getExtensionNumber());
        if (optionalExtension.isEmpty()) {
            throw new NoSuchElementException("Ramal não encontrado.");
        }

        Extension targetExtension = optionalExtension.get();

        if (targetExtension.getLoggedUser() != null) {
            throw new IllegalStateException("Este ramal já está em uso por outro usuário.");
        }

        // 1. Verifica se o usuário já está logado em outro ramal
        List<Extension> extensoesComUsuario = extensionRepository.findByLoggedUser(user);
        for (Extension e : extensoesComUsuario) {
            e.setLoggedUser(null); // Desloga
            e.setStatus(StatusExtension.DISPONIVEL);
            extensionRepository.save(e);
        }

        // 2. Loga o usuário no novo ramal
        targetExtension.setLoggedUser(user);
        targetExtension.setStatus(StatusExtension.OCUPADO);
        extensionRepository.save(targetExtension);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Login realizado com sucesso!");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<?> logoutUserFromExtension(@RequestBody ExtensionLoginRequest request) {
        // 1. Buscar usuário pelo username
        Optional<User> optionalUser = userRepository.findByUsername(request.getUsername());
        if (optionalUser.isEmpty()) {
            throw new NoSuchElementException("Usuário não encontrado.");
        }

        User user = optionalUser.get();

        // 2. Validar senha
        boolean senhaCorreta = passwordService.verifyPassword(request.getPassword(), user.getPassword());
        if (!senhaCorreta) {
            throw new SecurityException("Senha incorreta.");
        }

        // 3. Buscar ramal
        Optional<Extension> optionalExtension = extensionRepository.findByExtensionNumber(request.getExtensionNumber());
        if (optionalExtension.isEmpty()) {
            throw new NoSuchElementException("Ramal não encontrado.");
        }

        Extension extension = optionalExtension.get();

        // 4. Verificar se o usuário logado no ramal é o mesmo
        if (extension.getLoggedUser() == null || !extension.getLoggedUser().getId().equals(user.getId())) {
            throw new IllegalStateException("Você não está logado neste ramal.");
        }

        // 5. Desassociar usuário do ramal
        extension.setLoggedUser(null);
        extension.setStatus(StatusExtension.DISPONIVEL);
        extensionRepository.save(extension);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Logout realizado com sucesso.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Extension>> searchExtensions(
            @RequestParam String query,
            @PageableDefault(size = 8) Pageable pageable) {

        Page<Extension> result;

        try {
            // Se for número, busca pelo número do ramal
            Integer extensionNumber = Integer.parseInt(query);
            result = extensionRepository.findByExtensionNumber(extensionNumber, pageable);
        } catch (NumberFormatException e) {
            // Se não for número, busca pelo username do usuário logado
            result = extensionRepository.findByLoggedUser_UsernameContainingIgnoreCase(query, pageable);
        }

        return ResponseEntity.ok(result);
    }

    @PostMapping("/configure-range")
    public ResponseEntity<String> configureRange(@RequestBody ExtensionRangeDTO request) {
        try {
            extensionService.configureRange(request.getStart(), request.getEnd());
            return ResponseEntity.ok("Range configurado com sucesso!");
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("reset-range")
    public ResponseEntity<String> resetRange() {
        extensionService.resetRangeConfiguration();
        return ResponseEntity.ok("Range resetado com sucesso!");
    }

}
