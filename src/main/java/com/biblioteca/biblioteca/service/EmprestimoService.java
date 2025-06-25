package com.biblioteca.biblioteca.service;

import com.biblioteca.biblioteca.model.Emprestimo;
import com.biblioteca.biblioteca.model.Livro;
import com.biblioteca.biblioteca.model.Usuario;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EmprestimoService {
    List<Emprestimo> listarTodos();
    Optional<Emprestimo> buscarPorId(Long id);
    List<Emprestimo> buscarPorUsuario(Usuario usuario);
    List<Emprestimo> buscarPorLivro(Livro livro);
    List<Emprestimo> buscarPorStatus(Boolean devolvido);
    List<Emprestimo> buscarPorPeriodo(LocalDate inicio, LocalDate fim);
    List<Emprestimo> buscarEmprestimosAtivos(Usuario usuario);
    List<Emprestimo> buscarEmprestimosEmAtraso();
    Emprestimo realizarEmprestimo(Emprestimo emprestimo);
    Emprestimo realizarDevolucao(Long id);
    Emprestimo atualizar(Long id, Emprestimo emprestimo);
    void deletar(Long id);
}