package com.biblioteca.biblioteca.repository;

import com.biblioteca.biblioteca.model.Emprestimo;
import com.biblioteca.biblioteca.model.Livro;
import com.biblioteca.biblioteca.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {
    List<Emprestimo> findByUsuario(Usuario usuario);
    List<Emprestimo> findByLivro(Livro livro);
    List<Emprestimo> findByDevolvido(Boolean devolvido);
    List<Emprestimo> findByDataEmprestimoBetween(LocalDate inicio, LocalDate fim);
    List<Emprestimo> findByDataDevolucaoIsNull();
    List<Emprestimo> findByUsuarioAndDevolvido(Usuario usuario, Boolean devolvido);
}