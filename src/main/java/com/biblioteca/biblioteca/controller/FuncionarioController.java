package com.biblioteca.biblioteca.controller;

import com.biblioteca.biblioteca.dto.FuncionarioDTO;
import com.biblioteca.biblioteca.dto.LoginDTO;
import com.biblioteca.biblioteca.mapper.FuncionarioMapper;
import com.biblioteca.biblioteca.model.Funcionario;
import com.biblioteca.biblioteca.service.FuncionarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/funcionarios")
@CrossOrigin(origins = "*")
public class FuncionarioController {

    private final FuncionarioService funcionarioService;
    private final FuncionarioMapper funcionarioMapper;

    @Autowired
    public FuncionarioController(FuncionarioService funcionarioService, FuncionarioMapper funcionarioMapper) {
        this.funcionarioService = funcionarioService;
        this.funcionarioMapper = funcionarioMapper;
    }

    @GetMapping
    public ResponseEntity<List<FuncionarioDTO>> listarTodos() {
        List<Funcionario> funcionarios = funcionarioService.listarTodos();
        return ResponseEntity.ok(funcionarioMapper.toDTO(funcionarios));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FuncionarioDTO> buscarPorId(@PathVariable Long id) {
        Optional<Funcionario> funcionario = funcionarioService.buscarPorId(id);
        return funcionario.map(value -> ResponseEntity.ok(funcionarioMapper.toDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<FuncionarioDTO>> buscarPorNome(@PathVariable String nome) {
        List<Funcionario> funcionarios = funcionarioService.buscarPorNome(nome);
        return ResponseEntity.ok(funcionarioMapper.toDTO(funcionarios));
    }

    @GetMapping("/cargo/{cargo}")
    public ResponseEntity<List<FuncionarioDTO>> buscarPorCargo(@PathVariable String cargo) {
        List<Funcionario> funcionarios = funcionarioService.buscarPorCargo(cargo);
        return ResponseEntity.ok(funcionarioMapper.toDTO(funcionarios));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<FuncionarioDTO> buscarPorEmail(@PathVariable String email) {
        Optional<Funcionario> funcionario = funcionarioService.buscarPorEmail(email);
        return funcionario.map(value -> ResponseEntity.ok(funcionarioMapper.toDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<FuncionarioDTO> buscarPorUsername(@PathVariable String username) {
        Optional<Funcionario> funcionario = funcionarioService.buscarPorUsername(username);
        return funcionario.map(value -> ResponseEntity.ok(funcionarioMapper.toDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<FuncionarioDTO> criar(@Valid @RequestBody FuncionarioDTO funcionarioDTO) {
        // Verificar se username já existe
        if (funcionarioService.existePorUsername(funcionarioDTO.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        // Verificar se email já existe
        if (funcionarioDTO.getEmail() != null && funcionarioService.existePorEmail(funcionarioDTO.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Funcionario funcionario = funcionarioMapper.toEntity(funcionarioDTO);
        Funcionario funcionarioSalvo = funcionarioService.salvar(funcionario);
        return ResponseEntity.status(HttpStatus.CREATED).body(funcionarioMapper.toDTO(funcionarioSalvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FuncionarioDTO> atualizar(@PathVariable Long id, @Valid @RequestBody FuncionarioDTO funcionarioDTO) {
        Optional<Funcionario> funcionarioOpt = funcionarioService.buscarPorId(id);
        if (!funcionarioOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        // Verificar se username já existe para outro funcionário
        Optional<Funcionario> funcionarioExistente = funcionarioService.buscarPorUsername(funcionarioDTO.getUsername());
        if (funcionarioExistente.isPresent() && !funcionarioExistente.get().getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        // Verificar se email já existe para outro funcionário
        if (funcionarioDTO.getEmail() != null) {
            funcionarioExistente = funcionarioService.buscarPorEmail(funcionarioDTO.getEmail());
            if (funcionarioExistente.isPresent() && !funcionarioExistente.get().getId().equals(id)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        }

        Funcionario funcionarioExistenteBD = funcionarioOpt.get();
        funcionarioMapper.updateEntityFromDTO(funcionarioDTO, funcionarioExistenteBD);
        Funcionario funcionarioAtualizado = funcionarioService.atualizar(id, funcionarioExistenteBD);
        return ResponseEntity.ok(funcionarioMapper.toDTO(funcionarioAtualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!funcionarioService.buscarPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        funcionarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<FuncionarioDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        Optional<Funcionario> funcionario = funcionarioService.login(loginDTO.getUsername(), loginDTO.getPassword());
        
        if (funcionario.isPresent()) {
            FuncionarioDTO funcionarioDTO = funcionarioMapper.toDTO(funcionario.get());
            funcionarioDTO.setPassword(null); // Remove password from response
            return ResponseEntity.ok(funcionarioDTO);
        }
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}