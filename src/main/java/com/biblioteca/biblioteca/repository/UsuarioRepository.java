package com.biblioteca.biblioteca.repository;

import com.biblioteca.biblioteca.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    List<Usuario> findByNomeContainingIgnoreCase(String nome);
    Optional<Usuario> findByCpf(String cpf);
    Optional<Usuario> findByEmail(String email);
    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);
}