package io.github.cursodsousa.libraryapi.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;


@Schema(name = "Usuario")
public record UsuarioDTO(
        @NotBlank(message = "campo obrigatório")
        String login,
        @NotBlank(message = "campo obrigatório")
        String senha,
        @Email (message = "inválido")
        @NotBlank(message = "campo obrigatório")
        String email,
        List<String> roles) {
}
