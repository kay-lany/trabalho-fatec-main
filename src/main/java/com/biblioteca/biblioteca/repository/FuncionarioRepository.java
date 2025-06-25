package com.biblioteca.biblioteca.repository;

import com.biblioteca.biblioteca.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
    List<Funcionario> findByNomeContainingIgnoreCase(String nome);
    List<Funcionario> findByCargo(String cargo);
    Optional<Funcionario> findByEmail(String email);
    Optional<Funcionario> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}