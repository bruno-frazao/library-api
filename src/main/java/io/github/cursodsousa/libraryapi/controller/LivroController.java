package io.github.cursodsousa.libraryapi.controller;


import io.github.cursodsousa.libraryapi.controller.dto.CadastroLivroDTO;
import io.github.cursodsousa.libraryapi.controller.dto.ResultadoPesquisaLivroDTO;
import io.github.cursodsousa.libraryapi.controller.mappers.LivroMapper;
import io.github.cursodsousa.libraryapi.model.GeneroLivro;
import io.github.cursodsousa.libraryapi.model.Livro;
import io.github.cursodsousa.libraryapi.service.LivroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
// http://localhost:8080/livros/
@RequestMapping("/livros")
@RequiredArgsConstructor
@Tag(name = "Livros")
public class LivroController implements GenericController {


    private final LivroService service;
    private final LivroMapper mapper;

    @PostMapping
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "Salvar", description = "Cadastrar novo autor")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cadastro com sucesso"),
            @ApiResponse(responseCode = "422", description = "Erro de validação"),
            @ApiResponse(responseCode = "409", description = "Livro já cadastrado")
    })
    public ResponseEntity<Void> salvar(@RequestBody @Valid CadastroLivroDTO dto) {
        Livro livro = mapper.toEntity(dto);
        service.salvar(livro);
        URI location = gerarHeaderLocation(livro.getId());

        return ResponseEntity.created(location).build();
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "Obter Detalhes", description = "Retorna livro pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Livro encontrado"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado"),
    })
    public ResponseEntity<ResultadoPesquisaLivroDTO> obterDetalhes(@PathVariable("id") String id) {
        return service.obterPorId(UUID.fromString(id))
                .map(livro -> {
                    var dto = mapper.toDTO(livro);
                    return ResponseEntity.ok(dto);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "Deletar", description = "Deleta livro existente")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Livro deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado"),
    })
    public ResponseEntity<Object> deletar(@PathVariable("id") String id) {
        return service.obterPorId(UUID.fromString(id))
                .map(livro -> {
                    service.deletar(livro);
                    return ResponseEntity.noContent().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "Pesquisar", description = "Pesquisa livros por parâmetros")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sucesso"),
    })
    public ResponseEntity<Page<ResultadoPesquisaLivroDTO>> pesquisa(
            @RequestParam(value = "isbn", required = false)
            String isbn,
            @RequestParam(value = "titulo", required = false)
            String titulo,
            @RequestParam(value = "nome-autor", required = false)
            String nomeAutor,
            @RequestParam(value = "genero", required = false)
            GeneroLivro genero,
            @RequestParam(value = "ano-publicacao", required = false)
            Integer anoPublicacao,
            @RequestParam(value = "pagina", defaultValue = "0")
            Integer pagina,
            @RequestParam(value = "tamanho-pagina", defaultValue = "10")
            Integer tamanhoPagina
    ){
        Page<Livro> paginaResultado = service.pesquisa(
                isbn, titulo, nomeAutor, genero, anoPublicacao, pagina, tamanhoPagina);

        Page<ResultadoPesquisaLivroDTO> resultado = paginaResultado.map(mapper::toDTO);

//        var lista = paginaResultado
//                .stream()
//                .map(mapper::toDTO)
//                .collect(Collectors.toList());

        return ResponseEntity.ok(resultado);
    }


    @PutMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "Atualizar", description = "Atualiza livro existente")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado"),
            @ApiResponse(responseCode = "409", description = "Livro já cadastrado")
    })
    public ResponseEntity<Object> atualizar (
            @PathVariable("id") String id, @RequestBody @Valid CadastroLivroDTO dto) {
        return service.obterPorId(UUID.fromString(id))
                .map(livro -> {
                    Livro entidadeAux = mapper.toEntity(dto);

                    livro.setDataPublicacao(entidadeAux.getDataPublicacao());
                    livro.setIsbn(entidadeAux.getIsbn());
                    livro.setPreco(entidadeAux.getPreco());
                    livro.setGenero(entidadeAux.getGenero());
                    livro.setTitulo(entidadeAux.getTitulo());
                    livro.setAutor(entidadeAux.getAutor());

                    service.atualizar(livro);

                    return ResponseEntity.noContent().build();

                }).orElseGet( () -> ResponseEntity.notFound().build());

    }

}
