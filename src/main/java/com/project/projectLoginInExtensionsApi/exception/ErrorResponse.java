package com.project.projectLoginInExtensionsApi.exception;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// Essa classe representa o formato padronizado do erro
public class ErrorResponse {
    private int status; // Código HTTP(400, 404, 500 etc.)
    private String error; // Tipo de erro(Bad Request, Not found...)
    private String message; // Mensagem amigável sobre o erro
    private String path; // Caminho da requisição que gerou o erro
    private LocalDateTime timestamp; // momento do erro

    public ErrorResponse(int status, String error, String message, String path) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.timestamp = LocalDateTime.now();
    }
}
