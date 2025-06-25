package com.biblioteca.biblioteca.service;

import com.biblioteca.biblioteca.model.Categoria;

import java.util.List;
import java.util.Optional;

public interface CategoriaService {
    List<Categoria> listarTodas();
    Optional<Categoria> buscarPorId(Long id);
    Optional<Categoria> buscarPorNome(String nome);
    Categoria salvar(Categoria categoria);
    Categoria atualizar(Long id, Categoria categoria);
    void deletar(Long id);
    boolean existePorNome(String nome);
}