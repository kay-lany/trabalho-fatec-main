package com.biblioteca.biblioteca.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "livros")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(nullable = false, length = 100)
    private String autor;

    @Column(name = "ano_publicacao")
    private Integer anoPublicacao;

    @Column(nullable = false)
    private Boolean disponivel;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;
}
