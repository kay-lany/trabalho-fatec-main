package com.biblioteca.biblioteca.controller;

import com.biblioteca.biblioteca.dto.UsuarioDTO;
import com.biblioteca.biblioteca.mapper.UsuarioMapper;
import com.biblioteca.biblioteca.model.Usuario;
import com.biblioteca.biblioteca.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;

    @Autowired
    public UsuarioController(UsuarioService usuarioService, UsuarioMapper usuarioMapper) {
        this.usuarioService = usuarioService;
        this.usuarioMapper = usuarioMapper;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listarTodos() {
        List<Usuario> usuarios = usuarioService.listarTodos();
        return ResponseEntity.ok(usuarioMapper.toDTO(usuarios));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> buscarPorId(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.buscarPorId(id);
        return usuario.map(value -> ResponseEntity.ok(usuarioMapper.toDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<UsuarioDTO>> buscarPorNome(@PathVariable String nome) {
        List<Usuario> usuarios = usuarioService.buscarPorNome(nome);
        return ResponseEntity.ok(usuarioMapper.toDTO(usuarios));
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<UsuarioDTO> buscarPorCpf(@PathVariable String cpf) {
        Optional<Usuario> usuario = usuarioService.buscarPorCpf(cpf);
        return usuario.map(value -> ResponseEntity.ok(usuarioMapper.toDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UsuarioDTO> buscarPorEmail(@PathVariable String email) {
        Optional<Usuario> usuario = usuarioService.buscarPorEmail(email);
        return usuario.map(value -> ResponseEntity.ok(usuarioMapper.toDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> criar(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        // Verificar se CPF já existe
        if (usuarioDTO.getCpf() != null && usuarioService.existePorCpf(usuarioDTO.getCpf())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        // Verificar se email já existe
        if (usuarioDTO.getEmail() != null && usuarioService.existePorEmail(usuarioDTO.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Usuario usuario = usuarioMapper.toEntity(usuarioDTO);
        Usuario usuarioSalvo = usuarioService.salvar(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioMapper.toDTO(usuarioSalvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> atualizar(@PathVariable Long id, @Valid @RequestBody UsuarioDTO usuarioDTO) {
        if (!usuarioService.buscarPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        // Verificar se CPF já existe para outro usuário
        if (usuarioDTO.getCpf() != null) {
            Optional<Usuario> usuarioExistente = usuarioService.buscarPorCpf(usuarioDTO.getCpf());
            if (usuarioExistente.isPresent() && !usuarioExistente.get().getId().equals(id)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        }

        // Verificar se email já existe para outro usuário
        if (usuarioDTO.getEmail() != null) {
            Optional<Usuario> usuarioExistente = usuarioService.buscarPorEmail(usuarioDTO.getEmail());
            if (usuarioExistente.isPresent() && !usuarioExistente.get().getId().equals(id)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        }

        Usuario usuario = usuarioMapper.toEntity(usuarioDTO);
        Usuario usuarioAtualizado = usuarioService.atualizar(id, usuario);
        return ResponseEntity.ok(usuarioMapper.toDTO(usuarioAtualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!usuarioService.buscarPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}