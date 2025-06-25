package com.biblioteca.biblioteca.service;

import com.biblioteca.biblioteca.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    List<Usuario> listarTodos();
    Optional<Usuario> buscarPorId(Long id);
    List<Usuario> buscarPorNome(String nome);
    Optional<Usuario> buscarPorCpf(String cpf);
    Optional<Usuario> buscarPorEmail(String email);
    Usuario salvar(Usuario usuario);
    Usuario atualizar(Long id, Usuario usuario);
    void deletar(Long id);
    boolean existePorCpf(String cpf);
    boolean existePorEmail(String email);
}