package com.biblioteca.biblioteca.controller;

import com.biblioteca.biblioteca.dto.LivroDTO;
import com.biblioteca.biblioteca.mapper.LivroMapper;
import com.biblioteca.biblioteca.model.Categoria;
import com.biblioteca.biblioteca.model.Livro;
import com.biblioteca.biblioteca.service.CategoriaService;
import com.biblioteca.biblioteca.service.LivroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/livros")
@CrossOrigin(origins = "*")
public class LivroController {

    private final LivroService livroService;
    private final CategoriaService categoriaService;
    private final LivroMapper livroMapper;

    @Autowired
    public LivroController(LivroService livroService, CategoriaService categoriaService, LivroMapper livroMapper) {
        this.livroService = livroService;
        this.categoriaService = categoriaService;
        this.livroMapper = livroMapper;
    }

    @GetMapping
    public ResponseEntity<List<LivroDTO>> listarTodos() {
        List<Livro> livros = livroService.listarTodos();
        return ResponseEntity.ok(livroMapper.toDTO(livros));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LivroDTO> buscarPorId(@PathVariable Long id) {
        Optional<Livro> livro = livroService.buscarPorId(id);
        return livro.map(value -> ResponseEntity.ok(livroMapper.toDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/titulo/{titulo}")
    public ResponseEntity<List<LivroDTO>> buscarPorTitulo(@PathVariable String titulo) {
        List<Livro> livros = livroService.buscarPorTitulo(titulo);
        return ResponseEntity.ok(livroMapper.toDTO(livros));
    }

    @GetMapping("/autor/{autor}")
    public ResponseEntity<List<LivroDTO>> buscarPorAutor(@PathVariable String autor) {
        List<Livro> livros = livroService.buscarPorAutor(autor);
        return ResponseEntity.ok(livroMapper.toDTO(livros));
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<LivroDTO>> buscarPorCategoria(@PathVariable Long categoriaId) {
        Optional<Categoria> categoria = categoriaService.buscarPorId(categoriaId);
        if (!categoria.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        List<Livro> livros = livroService.buscarPorCategoria(categoria.get());
        return ResponseEntity.ok(livroMapper.toDTO(livros));
    }

    @GetMapping("/disponivel/{disponivel}")
    public ResponseEntity<List<LivroDTO>> buscarPorDisponibilidade(@PathVariable Boolean disponivel) {
        List<Livro> livros = livroService.buscarPorDisponibilidade(disponivel);
        return ResponseEntity.ok(livroMapper.toDTO(livros));
    }

    @GetMapping("/ano/{ano}")
    public ResponseEntity<List<LivroDTO>> buscarPorAnoPublicacao(@PathVariable Integer ano) {
        List<Livro> livros = livroService.buscarPorAnoPublicacao(ano);
        return ResponseEntity.ok(livroMapper.toDTO(livros));
    }

    @PostMapping
    public ResponseEntity<LivroDTO> criar(@Valid @RequestBody LivroDTO livroDTO) {
        // Verificar se a categoria existe
        if (livroDTO.getCategoriaId() != null && !categoriaService.buscarPorId(livroDTO.getCategoriaId()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Livro livro = livroMapper.toEntity(livroDTO);
        Livro livroSalvo = livroService.salvar(livro);
        return ResponseEntity.status(HttpStatus.CREATED).body(livroMapper.toDTO(livroSalvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LivroDTO> atualizar(@PathVariable Long id, @Valid @RequestBody LivroDTO livroDTO) {
        if (!livroService.buscarPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        // Verificar se a categoria existe
        if (livroDTO.getCategoriaId() != null && !categoriaService.buscarPorId(livroDTO.getCategoriaId()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Livro livro = livroMapper.toEntity(livroDTO);
        Livro livroAtualizado = livroService.atualizar(id, livro);
        return ResponseEntity.ok(livroMapper.toDTO(livroAtualizado));
    }

    @PatchMapping("/{id}/disponibilidade")
    public ResponseEntity<LivroDTO> alterarDisponibilidade(@PathVariable Long id, @RequestParam Boolean disponivel) {
        if (!livroService.buscarPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Livro livroAtualizado = livroService.alterarDisponibilidade(id, disponivel);
        return ResponseEntity.ok(livroMapper.toDTO(livroAtualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!livroService.buscarPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        livroService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}