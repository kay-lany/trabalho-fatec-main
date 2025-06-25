package com.biblioteca.biblioteca.controller;

import com.biblioteca.biblioteca.dto.CategoriaDTO;
import com.biblioteca.biblioteca.mapper.CategoriaMapper;
import com.biblioteca.biblioteca.model.Categoria;
import com.biblioteca.biblioteca.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "*")
public class CategoriaController {

    private final CategoriaService categoriaService;
    private final CategoriaMapper categoriaMapper;

    @Autowired
    public CategoriaController(CategoriaService categoriaService, CategoriaMapper categoriaMapper) {
        this.categoriaService = categoriaService;
        this.categoriaMapper = categoriaMapper;
    }

    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> listarTodas() {
        List<Categoria> categorias = categoriaService.listarTodas();
        return ResponseEntity.ok(categoriaMapper.toDTO(categorias));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDTO> buscarPorId(@PathVariable Long id) {
        Optional<Categoria> categoria = categoriaService.buscarPorId(id);
        return categoria.map(value -> ResponseEntity.ok(categoriaMapper.toDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CategoriaDTO> criar(@Valid @RequestBody CategoriaDTO categoriaDTO) {
        // Verificar se já existe uma categoria com o mesmo nome
        if (categoriaService.existePorNome(categoriaDTO.getNome())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Categoria categoria = categoriaMapper.toEntity(categoriaDTO);
        Categoria categoriaSalva = categoriaService.salvar(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaMapper.toDTO(categoriaSalva));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDTO> atualizar(@PathVariable Long id, @Valid @RequestBody CategoriaDTO categoriaDTO) {
        if (!categoriaService.buscarPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        // Verificar se já existe outra categoria com o mesmo nome
        Optional<Categoria> categoriaExistente = categoriaService.buscarPorNome(categoriaDTO.getNome());
        if (categoriaExistente.isPresent() && !categoriaExistente.get().getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Categoria categoria = categoriaMapper.toEntity(categoriaDTO);
        Categoria categoriaAtualizada = categoriaService.atualizar(id, categoria);
        return ResponseEntity.ok(categoriaMapper.toDTO(categoriaAtualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!categoriaService.buscarPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        categoriaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}