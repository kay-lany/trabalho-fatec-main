package com.biblioteca.biblioteca.service;

import com.biblioteca.biblioteca.model.Funcionario;

import java.util.List;
import java.util.Optional;

public interface FuncionarioService {
    List<Funcionario> listarTodos();
    Optional<Funcionario> buscarPorId(Long id);
    List<Funcionario> buscarPorNome(String nome);
    List<Funcionario> buscarPorCargo(String cargo);
    Optional<Funcionario> buscarPorEmail(String email);
    Optional<Funcionario> buscarPorUsername(String username);
    Funcionario salvar(Funcionario funcionario);
    Funcionario atualizar(Long id, Funcionario funcionario);
    void deletar(Long id);
    boolean existePorUsername(String username);
    boolean existePorEmail(String email);
    Optional<Funcionario> login(String username, String password);
}