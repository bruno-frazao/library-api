package io.github.cursodsousa.libraryapi.controller;


import io.github.cursodsousa.libraryapi.controller.dto.UsuarioDTO;
import io.github.cursodsousa.libraryapi.controller.mappers.UsuarioMapper;
import io.github.cursodsousa.libraryapi.repository.UsuarioRepository;
import io.github.cursodsousa.libraryapi.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios")
public class UsuarioController {

    private final UsuarioService service;
    private final UsuarioMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Salvar", description = "Cadastrar novo Usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cadastro com sucesso"),
            @ApiResponse(responseCode = "422", description = "Erro de validação"),
            @ApiResponse(responseCode = "409", description = "Usuário já cadastrado")
    })
    public void salvar(@RequestBody @Valid UsuarioDTO dto){
        var usuario = mapper.toEntity(dto);
        service.salvar(usuario);
    }
}
