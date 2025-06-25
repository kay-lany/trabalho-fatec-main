package com.biblioteca.biblioteca.controller;

import com.biblioteca.biblioteca.dto.EmprestimoDTO;
import com.biblioteca.biblioteca.mapper.EmprestimoMapper;
import com.biblioteca.biblioteca.model.Emprestimo;
import com.biblioteca.biblioteca.model.Livro;
import com.biblioteca.biblioteca.model.Usuario;
import com.biblioteca.biblioteca.service.EmprestimoService;
import com.biblioteca.biblioteca.service.LivroService;
import com.biblioteca.biblioteca.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/emprestimos")
@CrossOrigin(origins = "*")
public class EmprestimoController {

    private final EmprestimoService emprestimoService;
    private final UsuarioService usuarioService;
    private final LivroService livroService;
    private final EmprestimoMapper emprestimoMapper;

    @Autowired
    public EmprestimoController(EmprestimoService emprestimoService, UsuarioService usuarioService,
                               LivroService livroService, EmprestimoMapper emprestimoMapper) {
        this.emprestimoService = emprestimoService;
        this.usuarioService = usuarioService;
        this.livroService = livroService;
        this.emprestimoMapper = emprestimoMapper;
    }

    @GetMapping
    public ResponseEntity<List<EmprestimoDTO>> listarTodos() {
        List<Emprestimo> emprestimos = emprestimoService.listarTodos();
        return ResponseEntity.ok(emprestimoMapper.toDTO(emprestimos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmprestimoDTO> buscarPorId(@PathVariable Long id) {
        Optional<Emprestimo> emprestimo = emprestimoService.buscarPorId(id);
        return emprestimo.map(value -> ResponseEntity.ok(emprestimoMapper.toDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<EmprestimoDTO>> buscarPorUsuario(@PathVariable Long usuarioId) {
        Optional<Usuario> usuario = usuarioService.buscarPorId(usuarioId);
        if (!usuario.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        List<Emprestimo> emprestimos = emprestimoService.buscarPorUsuario(usuario.get());
        return ResponseEntity.ok(emprestimoMapper.toDTO(emprestimos));
    }

    @GetMapping("/livro/{livroId}")
    public ResponseEntity<List<EmprestimoDTO>> buscarPorLivro(@PathVariable Long livroId) {
        Optional<Livro> livro = livroService.buscarPorId(livroId);
        if (!livro.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        List<Emprestimo> emprestimos = emprestimoService.buscarPorLivro(livro.get());
        return ResponseEntity.ok(emprestimoMapper.toDTO(emprestimos));
    }

    @GetMapping("/status/{devolvido}")
    public ResponseEntity<List<EmprestimoDTO>> buscarPorStatus(@PathVariable Boolean devolvido) {
        List<Emprestimo> emprestimos = emprestimoService.buscarPorStatus(devolvido);
        return ResponseEntity.ok(emprestimoMapper.toDTO(emprestimos));
    }

    @GetMapping("/periodo")
    public ResponseEntity<List<EmprestimoDTO>> buscarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        List<Emprestimo> emprestimos = emprestimoService.buscarPorPeriodo(inicio, fim);
        return ResponseEntity.ok(emprestimoMapper.toDTO(emprestimos));
    }

    @GetMapping("/usuario/{usuarioId}/ativos")
    public ResponseEntity<List<EmprestimoDTO>> buscarEmprestimosAtivos(@PathVariable Long usuarioId) {
        Optional<Usuario> usuario = usuarioService.buscarPorId(usuarioId);
        if (!usuario.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        List<Emprestimo> emprestimos = emprestimoService.buscarEmprestimosAtivos(usuario.get());
        return ResponseEntity.ok(emprestimoMapper.toDTO(emprestimos));
    }

    @GetMapping("/atrasados")
    public ResponseEntity<List<EmprestimoDTO>> buscarEmprestimosEmAtraso() {
        List<Emprestimo> emprestimos = emprestimoService.buscarEmprestimosEmAtraso();
        return ResponseEntity.ok(emprestimoMapper.toDTO(emprestimos));
    }

    @PostMapping
    public ResponseEntity<EmprestimoDTO> realizarEmprestimo(@Valid @RequestBody EmprestimoDTO emprestimoDTO) {
        // Verificar se usuário existe
        if (!usuarioService.buscarPorId(emprestimoDTO.getUsuarioId()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // Verificar se livro existe
        Optional<Livro> livroOpt = livroService.buscarPorId(emprestimoDTO.getLivroId());
        if (!livroOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // Verificar se o livro está disponível
        Livro livro = livroOpt.get();
        if (!livro.getDisponivel()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Emprestimo emprestimo = emprestimoMapper.toEntity(emprestimoDTO);
        
        // Se não for informada a data de empréstimo, definir como hoje
        if (emprestimo.getDataEmprestimo() == null) {
            emprestimo.setDataEmprestimo(LocalDate.now());
        }
        
        // Inicialmente, o empréstimo não está devolvido
        emprestimo.setDevolvido(false);
        
        Emprestimo emprestimoRealizado = emprestimoService.realizarEmprestimo(emprestimo);
        return ResponseEntity.status(HttpStatus.CREATED).body(emprestimoMapper.toDTO(emprestimoRealizado));
    }

    @PatchMapping("/{id}/devolucao")
    public ResponseEntity<EmprestimoDTO> realizarDevolucao(@PathVariable Long id) {
        if (!emprestimoService.buscarPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Emprestimo emprestimoDevolucao = emprestimoService.realizarDevolucao(id);
        return ResponseEntity.ok(emprestimoMapper.toDTO(emprestimoDevolucao));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmprestimoDTO> atualizar(@PathVariable Long id, @Valid @RequestBody EmprestimoDTO emprestimoDTO) {
        if (!emprestimoService.buscarPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        // Verificar se usuário existe
        if (!usuarioService.buscarPorId(emprestimoDTO.getUsuarioId()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // Verificar se livro existe
        if (!livroService.buscarPorId(emprestimoDTO.getLivroId()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Emprestimo emprestimo = emprestimoMapper.toEntity(emprestimoDTO);
        Emprestimo emprestimoAtualizado = emprestimoService.atualizar(id, emprestimo);
        return ResponseEntity.ok(emprestimoMapper.toDTO(emprestimoAtualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!emprestimoService.buscarPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        emprestimoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}