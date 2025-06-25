package com.biblioteca.biblioteca.service;

import com.biblioteca.biblioteca.model.Livro;
import com.biblioteca.biblioteca.model.Categoria;

import java.util.List;
import java.util.Optional;

public interface LivroService {
    List<Livro> listarTodos();
    Optional<Livro> buscarPorId(Long id);
    List<Livro> buscarPorTitulo(String titulo);
    List<Livro> buscarPorAutor(String autor);
    List<Livro> buscarPorCategoria(Categoria categoria);
    List<Livro> buscarPorDisponibilidade(Boolean disponivel);
    List<Livro> buscarPorAnoPublicacao(Integer ano);
    Livro salvar(Livro livro);
    Livro atualizar(Long id, Livro livro);
    void deletar(Long id);
    Livro alterarDisponibilidade(Long id, Boolean disponivel);
}