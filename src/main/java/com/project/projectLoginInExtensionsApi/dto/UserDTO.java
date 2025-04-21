package com.project.projectLoginInExtensionsApi.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    @NotBlank(message = "Usuário é obrigatório")
    @Column(unique = true)
    private String username;

    @NotBlank(message = "Senha é obrigatório")
    private String password;
}
