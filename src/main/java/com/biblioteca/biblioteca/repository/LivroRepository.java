package com.biblioteca.biblioteca.repository;

import com.biblioteca.biblioteca.model.Livro;
import com.biblioteca.biblioteca.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {
    List<Livro> findByTituloContainingIgnoreCase(String titulo);
    List<Livro> findByAutorContainingIgnoreCase(String autor);
    List<Livro> findByCategoria(Categoria categoria);
    List<Livro> findByDisponivel(Boolean disponivel);
    List<Livro> findByAnoPublicacao(Integer ano);
}